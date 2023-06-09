package pl.przemyslawpitus.mamyklocga.domain.game

import pl.przemyslawpitus.mamyklocga.domain.User

class PointsCounter {
    fun countPointsForGame(game: Game): Map<User, Int> {
        return game.rounds
            .map { countPointsForRound(it) }
            .flatMap { it.entries }
            .associate { it.toPair() }
    }

    private fun countPointsForRound(round: Round): Map<User, Int> {
        return round.builds
            .map { countPointsForBuild(it) }
            .flatMap { it.entries }
            .associate { it.toPair() }
    }

    private fun countPointsForBuild(build: Build): Map<User, Int> {
        val pointsPerUser = mutableMapOf<User, Int>()

        if (build.correctAnswerBy != null) {
            pointsPerUser.addPointForUser(build.builder)
            pointsPerUser.addPointForUser(build.correctAnswerBy)
        }

        if (build.hasPassedChallenge) {
            pointsPerUser.addPointForUser(build.builder)
        }

        return pointsPerUser
    }

    private fun MutableMap<User, Int>.addPointForUser(user: User) = this.merge(user, 1, Int::plus)
}