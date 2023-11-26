package pl.przemyslawpitus.mamyklocga.rounds

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import pl.przemyslawpitus.mamyklocga.domain.game.ChallengeProvider
import pl.przemyslawpitus.mamyklocga.domain.game.RoundsCreator
import pl.przemyslawpitus.mamyklocga.rounds.utils.randomUser
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoundsCreatorTest {

    @ParameterizedTest
    @MethodSource("roundsDependingOnUsersCount")
    fun `rounds count should depend on the number of players`(usersCount: Int, expectedRoundsCount: Int) {
        // given
        val challengeProvider = ChallengeProvider()
        val roundsCreator = RoundsCreator(
            challengeProvider = challengeProvider
        )
        val users = creatUsers(usersCount)

        // when
        val rounds = roundsCreator.createRounds(users)

        // then
        rounds.size shouldBe expectedRoundsCount
    }

    private fun roundsDependingOnUsersCount() = Stream.of(
        Arguments.of(2, 4),
        Arguments.of(3, 6),
        Arguments.of(4, 4),
        Arguments.of(5, 5),
        Arguments.of(10, 10),
    )

    @RepeatedTest(5)
    fun `players order should be the same in doubled rounds`() {
        // given
        val challengeProvider = ChallengeProvider()
        val roundsCreator = RoundsCreator(
            challengeProvider = challengeProvider
        )
        val users = creatUsers(3)

        // when
        val rounds = roundsCreator.createRounds(users)

        // then
        rounds.size shouldBe 6
        rounds[0].guesser shouldBe rounds[3].guesser
        rounds[1].guesser shouldBe rounds[4].guesser
        rounds[2].guesser shouldBe rounds[5].guesser
    }

    private fun creatUsers(count: Int) = List(count) { randomUser() }.toSet()
}