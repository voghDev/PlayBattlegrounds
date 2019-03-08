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

data class PlayerSeasonGameModeStats(
    val killPoints: Float = 0f,
    val kills: Int = 0,
    val losses: Int = 0,
    val top10s: Int = 0,
    val winPoints: Float = 0f,
    val roundsPlayed: Int = 0,
    val wins: Int = 0
) {
    fun isEmpty(): Boolean =
        kills == 0 && losses == 0

    fun kdr(): Float {
        val deaths: Int = roundsPlayed.minus(wins)
        return kills?.toFloat()?.div(maxOf(deaths, 1))
    }
}