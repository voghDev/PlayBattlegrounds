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
import es.voghdev.playbattlegrounds.features.matches.Match
import es.voghdev.playbattlegrounds.features.matches.db.MatchDBEntry.Companion.NAME

@Table(name = NAME, database = AppDatabase::class, allFields = true)
class MatchDBEntry : BaseModel {
    companion object {
        const val NAME = "MatchDBEntry"
    }

    @PrimaryKey
    @Column
    var id: String = ""
    @Column
    var date: Long = 0L
    @Column
    var gameMode: String = ""
    @Column
    var map: String = ""
    @Column
    var durationInSeconds: Int = 0
    @Column
    var numberOfKillsForCurrentPlayer: Int = 0
    @Column
    var placeForCurrentPlayer: Int = 0

    constructor() {
        // Mandatory for DBFlow
    }

    constructor(match: Match) {
        id = match.id
        date = match.date
        gameMode = match.gameMode
        map = match.map
        durationInSeconds = match.durationInSeconds
        numberOfKillsForCurrentPlayer = match.numberOfKillsForCurrentPlayer
        placeForCurrentPlayer = match.placeForCurrentPlayer
    }

    fun toDomain(): Match {
        return Match(
                id,
                date,
                gameMode,
                map,
                durationInSeconds,
                emptyList(),
                numberOfKillsForCurrentPlayer,
                placeForCurrentPlayer
        )
    }
}
