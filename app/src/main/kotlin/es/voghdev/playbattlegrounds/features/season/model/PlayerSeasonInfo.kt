/*
 * Copyright (C) 2018 Olmo Gallegos Hernández.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.voghdev.playbattlegrounds.features.season.model

import es.voghdev.playbattlegrounds.common.ui.ListEntity
import kotlin.math.roundToInt

data class PlayerSeasonInfo(
        val statsDuo: PlayerSeasonGameModeStats,
        val statsDuoFPP: PlayerSeasonGameModeStats,
        val statsSolo: PlayerSeasonGameModeStats,
        val statsSoloFPP: PlayerSeasonGameModeStats,
        val statsSquad: PlayerSeasonGameModeStats,
        val statsSquadFPP: PlayerSeasonGameModeStats
) : ListEntity() {
    fun getMaximumRating(): Int {
        return getRatingForGameModeStats(getBestRatingStats())
    }

    fun getMaximumKillDeathRatio(): Float {
        val bestStats = getBestKDRStats()

        val deaths: Int = maxOf(bestStats?.roundsPlayed?.minus(bestStats?.wins) ?: 1, 1)

        return bestStats?.kills?.toFloat()?.div(deaths) ?: 0f
    }

    fun getBestRatingStats(): PlayerSeasonGameModeStats? {
        val allStats = listOf(
                statsDuo, statsDuoFPP,
                statsSolo, statsSoloFPP,
                statsSquad, statsSquadFPP
        )

        val bestStats: PlayerSeasonGameModeStats? =
                allStats.maxBy { getRatingForGameModeStats(it) }
        return bestStats
    }

    fun getBestKDRStats(): PlayerSeasonGameModeStats? {
        val allStats = listOf(
                statsDuo, statsDuoFPP,
                statsSolo, statsSoloFPP,
                statsSquad, statsSquadFPP
        )

        val bestStats: PlayerSeasonGameModeStats? =
                allStats.maxBy { getKillDeathRatioForGameModeStats(it) }
        return bestStats
    }

    fun getKillDeathRatioForGameModeStats(stats: PlayerSeasonGameModeStats?): Float {
        val deaths: Int = stats?.roundsPlayed?.minus(stats?.wins) ?: 1
        return stats?.kills?.toFloat()?.div(maxOf(deaths, 1)) ?: 0f
    }

    fun getRatingForGameModeStats(stats: PlayerSeasonGameModeStats?): Int {
        val winPoints = stats?.winPoints ?: 0f
        val killPoints = stats?.killPoints ?: 0f

        return (winPoints + .2f * killPoints).roundToInt()
    }
}