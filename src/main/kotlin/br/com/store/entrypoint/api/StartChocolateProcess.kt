package br.com.store.entrypoint.api

import br.com.store.core.flow.ChocolateFlowService
import br.com.store.shared.dto.ChocolateStepEvent
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class StartChocolateProcess(
    private val chocolateFlowService: ChocolateFlowService,
) {

    @GetMapping("/start-chocolate-production-flow", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun startChocolateFlowParallel(): Flux<ChocolateStepEvent> {
        return chocolateFlowService.produceChocolate()
    }
}
