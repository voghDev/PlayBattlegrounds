package es.voghdev.playbattlegrounds.features.season.api.model

class GameModeStatsApiEntry(
        val killPoints: Float? = 0f,
        val kills: Int = 0,
        val top10s: Int = 0,
        val winPoints: Float = 0f,
        val wins: Int = 0
)