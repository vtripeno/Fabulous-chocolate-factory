package br.com.store.shared.extensions

import br.com.store.shared.dto.ChocolateStepEvent
import java.time.Instant
import reactor.core.publisher.Mono

fun monoToEvent(messageMono: Mono<String>): Mono<ChocolateStepEvent> {
    return messageMono.map { msg ->
        ChocolateStepEvent(
            timestamp = Instant.now().toString(),
            message = msg
        )
    }
}
