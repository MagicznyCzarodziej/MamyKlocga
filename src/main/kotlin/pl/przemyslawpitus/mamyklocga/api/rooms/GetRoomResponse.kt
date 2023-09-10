package pl.przemyslawpitus.mamyklocga.api.rooms

data class GetRoomResponse(
    val code: String,
    val name: String,
    val isRoomOwner: Boolean,
    val users: List<User>,
    val state: String,
    val game: Game?,
) {
    data class User(
        val username: String,
    )

    data class Game(
        val roundsTotal: Int,
        val currentRound: Round,
        val myPoints: Int,
        val words: List<String>,
    )

    data class Round(
        val roundNumber: Int,
        val role: String,
        val guesser: User,
        val challenge: String,
        val endsAt: String?,
        val state: String,

        val users: List<RoundUser>,
        val hasEveryoneRated: Boolean,
        val hasRatedGuesserGuess: Boolean,
        val hasRatedStolenGuess: Boolean,
    )

    data class RoundUser(
        val userId: String,
        val username: String,
        val role: String,
        val hasPassedChallenge: Boolean,
        val hasGuessedCorrectly: Boolean,
    )
}