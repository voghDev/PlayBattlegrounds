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

import arrow.core.Either
import com.raizlabs.android.dbflow.sql.language.SQLite
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.features.matches.Match
import es.voghdev.playbattlegrounds.features.matches.usecase.GetMatchById

class GetMatchByIdDBDataSource : GetMatchById {
    override fun getMatchById(id: String): Either<AbsError, Match> {
        val match = SQLite.select()
                .from(MatchDBEntry::class.java)
                .where(MatchDBEntry_Table.id.`is`(id))
                .queryList()
                .firstOrNull()?.toDomain()

        return if (match is Match)
            Either.right(match)
        else
            Either.left(AbsError("Match $id not found"))
    }
}