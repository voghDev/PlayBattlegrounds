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

import es.voghdev.playbattlegrounds.features.matches.MatchParticipant

class ParticipantByIdApiEntry(
        val id: String,
        val type: String,
        val attributes: ParticipantAttributesApiEntry?
) {
    fun getName() : String = attributes?.stats?.name ?: ""
    fun getPlace() : Int = attributes?.stats?.winPlace ?: 0
    fun getNumberOfKills(): Int = attributes?.stats?.kills ?: 0

    fun toDomain() : MatchParticipant {
        return MatchParticipant(id, getName(), getNumberOfKills(), getPlace())
    }
}
