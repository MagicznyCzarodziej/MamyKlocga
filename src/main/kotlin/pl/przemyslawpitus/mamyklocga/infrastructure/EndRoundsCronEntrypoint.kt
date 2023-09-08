package pl.przemyslawpitus.mamyklocga.infrastructure

import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.game.endRoundsUseCase.EndRoundsUseCase

@Service
@EnableScheduling
class EndRoundsCronEntrypoint(
    private val endRoundsUseCase: EndRoundsUseCase,
) {
    @Scheduled(fixedRate = 1000)
    fun endRounds() {
//        logger.info("Ending rounds")
        endRoundsUseCase.endRounds()
    }

    private companion object : WithLogger()
}