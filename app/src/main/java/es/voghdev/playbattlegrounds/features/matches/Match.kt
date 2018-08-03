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
package es.voghdev.playbattlegrounds.features.matches

import es.voghdev.playbattlegrounds.common.ui.ListEntity

data class Match(
    val id: String = "",
    var date: Long = 0L,
    var gameMode: String = "",
    val map: String = "",
    val durationInSeconds: Int = 0,
    var participants: List<MatchParticipant> = emptyList(),
    var numberOfKillsForCurrentPlayer: Int = 0,
    var placeForCurrentPlayer: Int = 0
) : ListEntity() {
    fun getNumberOfKills(participantName: String): Int {
        return participants.filter { it.name == participantName }.firstOrNull()?.kills ?: 0
    }

    fun getWinPlaceForParticipant(participantName: String): Int {
        return participants.filter { it.name == participantName }.firstOrNull()?.place ?: 0
    }

    fun isDuoOrSquad(): Boolean =
        gameMode.contains("duo") || gameMode.contains("squad") ||
            gameMode.contains("warmode") || gameMode.contains("pgi")
}