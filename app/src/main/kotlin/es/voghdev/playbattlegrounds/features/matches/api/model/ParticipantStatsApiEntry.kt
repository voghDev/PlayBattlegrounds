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
package es.voghdev.playbattlegrounds.features.matches.api.model

class ParticipantStatsApiEntry(
        val DBNOs: Int?,
        val assists: Int?,
        val boosts: Int?,
        val damageDealt: Float?,
        val deathType: String?,
        val headshotKills: Int?,
        val heals: Int?,
        val killPlace: Int?,
        val killPoints: Int?,
        val killPointsDelta: Float?,
        val killStreaks: Int?,
        val kills: Int?,
        val lastKillPoints: Int?,
        val lastWinPoints: Int?,
        val longestKill: Int?,
        val mostDamage: Int?,
        val name: String?,
        val playerId: String?,
        val revives: Int?,
        val rideDistance: Float?,
        val roadKills: Int?,
        val teamKills: Int?,
        val timeSurvived: Int?,
        val vehicleDestroys: Int?,
        val walkDistance: Float?,
        val weaponsAcquired: Int?,
        val winPlace: Int?,
        val winPoints: Int?,
        val winPointsDelta: Float?
)