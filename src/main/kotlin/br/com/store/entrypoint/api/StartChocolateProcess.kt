package br.com.store.entrypoint.api

import java.time.Duration
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class StartChocolateProcess {

    @GetMapping("/sse-flow", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun startChocolateFlow(): Flux<String> {
        return Flux.just(
            "Starting chocolate production process...",
            "Mixing ingredients...",
            "Heating the mixture...",
            "Pouring into molds...",
            "Cooling down...",
            "Packaging chocolates...",
            "Chocolate production process completed!"
        ).delayElements(Duration.ofSeconds(2))
    }
}
