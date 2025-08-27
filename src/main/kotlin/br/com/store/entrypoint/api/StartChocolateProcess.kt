package br.com.store.entrypoint.api

import br.com.store.core.IngredientsService
import br.com.store.core.KitchenService
import br.com.store.core.PostKitchenService
import java.time.Duration
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class StartChocolateProcess(
    private val ingredientsService: IngredientsService,
    private val kitchenService: KitchenService,
    private val postKitchenService: PostKitchenService,
) {

    @GetMapping("/start-chocolate-production-flow", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun startChocolateFlowParallel(): Flux<String> {

        val start = Mono.just("Starting chocolate production process...")
            .delayElement(Duration.ofSeconds(1))

        val mixing = Mono.fromCallable { ingredientsService.mix() }
            .delayElement(Duration.ofSeconds(2))

        val heating = Mono.fromCallable { kitchenService.heatMixture() }
            .delayElement(Duration.ofSeconds(2))

        val pouring = Mono.fromCallable { kitchenService.pourIntoMolds() }
            .delayElement(Duration.ofSeconds(2))

        val cooling = Mono.fromCallable { kitchenService.coolDown() }
            .delayElement(Duration.ofSeconds(3))

        val packaging = Mono.fromCallable { postKitchenService.packaging() }
            .delayElement(Duration.ofSeconds(2))

        val completed = Mono.fromCallable { postKitchenService.completed() }
            .delayElement(Duration.ofSeconds(1))

        // concat mantém a ordem
        return Flux.concat(
            start,
            mixing,
            heating,
            pouring,
            // cooling e packaging em paralelo
            Flux.merge(cooling, packaging),
            completed
        )
    }
}
