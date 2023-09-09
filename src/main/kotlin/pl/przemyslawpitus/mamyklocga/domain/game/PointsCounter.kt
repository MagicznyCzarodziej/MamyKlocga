package pl.przemyslawpitus.mamyklocga.domain.game

import pl.przemyslawpitus.mamyklocga.domain.user.UserId

class PointsCounter {
    fun countPointsForGame(game: Game): Map<UserId, Int> {
        return (listOf(game.currentRound) + game.pastRounds)
            .map { countPointsForRound(it) }
            .flatMap { it.entries }
            .associate { it.toPair() }
            .withDefault { 0 }
    }

    private fun countPointsForRound(round: Round): Map<UserId, Int> {
        return round.builds
            .map { countPointsForBuild(it) }
            .flatMap { it.entries }
            .associate { it.toPair() }
    }

    private fun countPointsForBuild(build: Build): Map<UserId, Int> {
        val pointsPerUser = mutableMapOf<UserId, Int>()

        if (build.correctAnswerBy != null) {
            pointsPerUser.addPointForUser(build.builder.userId)
            pointsPerUser.addPointForUser(build.correctAnswerBy.userId)
        }

        if (build.hasPassedChallenge) {
            pointsPerUser.addPointForUser(build.builder.userId)
        }

        return pointsPerUser
    }

    private fun MutableMap<UserId, Int>.addPointForUser(userId: UserId) = this.merge(userId, 1, Int::plus)
}