package es.voghdev.playbattlegrounds.features.season.api.model

import es.voghdev.playbattlegrounds.features.season.model.PlayerSeasonGameModeStats

class GameModeStatsApiEntry(
        val killPoints: Float? = 0f,
        val kills: Int? = 0,
        val losses: Int? = 0,
        val top10s: Int? = 0,
        val roundsPlayed: Int? = 0,
        val winPoints: Float? = 0f,
        val wins: Int? = 0
) {
    fun toDomain(): PlayerSeasonGameModeStats {
        return PlayerSeasonGameModeStats(
                killPoints ?: 0f,
                kills ?: 0,
                losses ?: 0,
                top10s ?: 0,
                winPoints ?: 0f,
                roundsPlayed ?: 0,
                wins ?: 0
        )
    }
}