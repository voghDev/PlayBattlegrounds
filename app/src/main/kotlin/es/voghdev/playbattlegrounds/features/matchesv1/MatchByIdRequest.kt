package es.voghdev.playbattlegrounds.features.matchesv1

data class MatchByIdRequest(
        val platform: String,
        val region: String,
        val apiKey: String,
        val id: String
)
