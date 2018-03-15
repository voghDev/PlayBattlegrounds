package es.voghdev.playbattlegrounds.features.matches

data class MatchesRequest(
        val platform: String,
        val region: String,
        val apiKey: String
)
