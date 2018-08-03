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
package es.voghdev.playbattlegrounds.features.season.api.model

import com.google.gson.annotations.SerializedName

class GameModeStatsSetApiEntry(
    @SerializedName("duo") val duo: GameModeStatsApiEntry?,
    @SerializedName("duo-fpp") val duoFPP: GameModeStatsApiEntry?,
    @SerializedName("solo") val solo: GameModeStatsApiEntry?,
    @SerializedName("solo-fpp") val soloFPP: GameModeStatsApiEntry?,
    @SerializedName("squad") val squad: GameModeStatsApiEntry?,
    @SerializedName("squad-fpp") val squadFPP: GameModeStatsApiEntry?
)
