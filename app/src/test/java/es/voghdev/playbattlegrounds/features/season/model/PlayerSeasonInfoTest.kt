package es.voghdev.playbattlegrounds.features.season.model

import org.junit.Assert.assertEquals
import org.junit.Test

class PlayerSeasonInfoTest {
    @Test
    fun `should not return NaN as KDR when player season stats are empty`() {
        val emptyStats = PlayerSeasonInfo(
                PlayerSeasonGameModeStats(),
                PlayerSeasonGameModeStats(),
                PlayerSeasonGameModeStats(),
                PlayerSeasonGameModeStats(),
                PlayerSeasonGameModeStats(),
                PlayerSeasonGameModeStats()
        )

        val bestGameMode = emptyStats.getBestKDRStats()
        val kdr = emptyStats.getKillDeathRatioForGameModeStats(bestGameMode)

        assertEquals(0f, kdr)
    }
}