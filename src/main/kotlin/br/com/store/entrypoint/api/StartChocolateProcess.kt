package br.com.store.entrypoint.api

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class StartChocolateProcess {

    @GetMapping("/start-chocolate-production-flow", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
suspend fun startChocolateFlowCoroutine(): Flux<String> = Flux.create { sink ->

    kotlinx.coroutines.runBlocking {
            val mix = async {
                delay(2000)
                "Mixing ingredients..."
            }

            val heat = async {
                delay(3000)
                "Heating the mixture..."
            }

            // mold depende de mix + heat
            val mold = async {
                mix.await()
                heat.await()
                delay(1000)
                "Pouring into molds..."
            }

            val cool = async {
                mold.await()
                delay(2000)
                "Cooling down..."
            }

            val pack = async {
                cool.await()
                delay(1000)
                "Packaging chocolates..."
            }

            listOf(mix, heat, mold, cool, pack).forEach { job ->
                sink.next(job.await())
            }

            sink.next("Chocolate production completed!")
            sink.complete()
        }
    }
}
