package pl.przemyslawpitus.mamyklocga.domain.game

val challenges = setOf( // TODO: Move to configuration
    Challenge(
        challengeId = "HIGHEST_BUILD",
        text = "Zbuduj najwyższą budowlę"
    ),
    Challenge(
        challengeId = "HIGHEST_BUILD",
        text = "Zbuduj najszybciej"
    ),
    Challenge(
        challengeId = "LEAST_BLOCKS",
        text = "Użyj najmniej kostek"
    ),
    Challenge(
        challengeId = "MOST_BLOCKS",
        text = "Użyj najwięcej kostek"
    ),
    Challenge(
        challengeId = "ONLY_ONE_COLOR",
        text = "Użyj tylko jednego koloru"
    ),
    Challenge(
        challengeId = "MOST_COLORS",
        text = "Użyj najwięcej kolorów"
    ),
    Challenge(
        challengeId = "ONLY_ONE_LAYER",
        text = "Tylko 1 warstwa"
    ),
)

class ChallengeProvider {
    fun getRandomChallenge() = challenges.random()
}

data class Challenge(
    val challengeId: String, // TODO make ChallengeId
    val text: String,
)