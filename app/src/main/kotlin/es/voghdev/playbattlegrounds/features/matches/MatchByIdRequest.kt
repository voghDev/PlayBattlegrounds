package es.voghdev.playbattlegrounds.features.matches

data class MatchByIdRequest(
        val platform: String,
        val region: String,
        val apiKey: String,
        val id: String
)
