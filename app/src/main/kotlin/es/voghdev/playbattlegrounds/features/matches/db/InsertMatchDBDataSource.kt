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

import com.raizlabs.android.dbflow.config.FlowManager
import es.voghdev.playbattlegrounds.common.db.AppDatabase
import es.voghdev.playbattlegrounds.features.matches.Match
import es.voghdev.playbattlegrounds.features.matches.usecase.InsertMatch

class InsertMatchDBDataSource : InsertMatch {
    override fun insertMatch(match: Match) {
        insertParticipants(match)

        MatchDBEntry(match).save()
    }

    private fun insertParticipants(match: Match) {
        if (match.participants.isNotEmpty())
            FlowManager.getDatabase(AppDatabase.javaClass)
                    .beginTransactionAsync {
                        match.participants.forEach {
                            it.matchId = match.id
                            MatchParticipantDBEntry(it).save()
                        }
                    }
    }
}
