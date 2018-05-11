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
package es.voghdev.playbattlegrounds.features.matches.db

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import es.voghdev.playbattlegrounds.common.db.AppDatabase
import es.voghdev.playbattlegrounds.features.matches.MatchParticipant
import es.voghdev.playbattlegrounds.features.matches.db.MatchParticipantDBEntry.Companion.NAME

@Table(name = NAME, database = AppDatabase::class, allFields = true)
class MatchParticipantDBEntry : BaseModel {
    companion object {
        const val NAME = "MatchParticipantDBEntry"
    }

    @PrimaryKey
    @Column
    var id: String = ""
    @Column
    var name: String = ""
    @Column
    var kills: Int = 0
    @Column
    var place: Int = 0

    constructor() {
        // Mandatory for DBFlow
    }

    constructor(participant: MatchParticipant) {
        participant.let {
            this.id = id
            this.name = name
            this.kills = kills
            this.place = place
        }
    }

    fun toDomain(): MatchParticipant {
        return MatchParticipant(
                id,
                name,
                kills,
                place
        )
    }
}
