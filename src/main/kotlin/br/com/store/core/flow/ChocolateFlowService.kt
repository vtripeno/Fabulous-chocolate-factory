package br.com.store.core.flow

import br.com.store.core.IngredientsService
import br.com.store.core.KitchenService
import br.com.store.core.PostKitchenService
import br.com.store.shared.dto.ChocolateStepEvent
import br.com.store.shared.extensions.monoToEvent
import java.time.Duration
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ChocolateFlowService(
    private val ingredientsService: IngredientsService,
    private val kitchenService: KitchenService,
    private val postKitchenService: PostKitchenService
) {
    fun produceChocolate(): Flux<ChocolateStepEvent> {
        val start: Mono<ChocolateStepEvent> = monoToEvent(
            Mono.just("Starting chocolate production process...")
                .delayElement(Duration.ofSeconds(1))
        )

        // Etapas dependentes
        val mixing: Mono<ChocolateStepEvent> = start.flatMap {
            monoToEvent(Mono.fromCallable { ingredientsService.mix() }
                .delayElement(Duration.ofSeconds(2)))
        }

        val heating: Mono<ChocolateStepEvent> = mixing.flatMap { mixEvent ->
            monoToEvent(Mono.fromCallable { kitchenService.heatMixture(mixEvent.message) }
                .delayElement(Duration.ofSeconds(2)))
        }

        val pouring: Mono<ChocolateStepEvent> = heating.flatMap { heatEvent ->
            monoToEvent(Mono.fromCallable { kitchenService.pourIntoMolds() }
                .delayElement(Duration.ofSeconds(2)))
        }

        // cooling e packaging dependem de pouring, mas podem rodar em paralelo
        val cooling: Mono<ChocolateStepEvent> = pouring.flatMap {
            monoToEvent(Mono.fromCallable { kitchenService.coolDown() }
                .delayElement(Duration.ofSeconds(3)))
                .flatMap { result ->
                    if (1 == 1) {
                        monoToEvent(Mono.just("Cooling finished"))
                    } else {
                        Mono.empty()  // não emite nada
                    }
                }
        }

        val packaging: Mono<ChocolateStepEvent> = pouring.flatMap {
            monoToEvent(Mono.fromCallable { postKitchenService.packaging() }
                .delayElement(Duration.ofSeconds(2)))
        }

        val parallel: Flux<ChocolateStepEvent> = Flux.merge(cooling, packaging)

        // etapa final depende do parallel
        val completed: Mono<ChocolateStepEvent> = parallel.then(
            monoToEvent(Mono.fromCallable { postKitchenService.completed() }
                .delayElement(Duration.ofSeconds(1)))
        )

        // concat mantém ordem: start -> mixing -> heating -> pouring -> parallel -> completed
        return Flux.concat(start, mixing, heating, pouring, parallel, completed)
    }
}
