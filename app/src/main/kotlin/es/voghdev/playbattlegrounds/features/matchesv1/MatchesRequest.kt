package es.voghdev.playbattlegrounds.features.matchesv1

data class MatchesRequest(
        val platform: String,
        val region: String,
        val apiKey: String
)
