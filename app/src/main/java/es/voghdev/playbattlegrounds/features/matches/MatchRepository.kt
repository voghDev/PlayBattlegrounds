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

import arrow.core.Either
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.common.Success
import es.voghdev.playbattlegrounds.features.matches.usecase.GetMatchById
import es.voghdev.playbattlegrounds.features.matches.usecase.InsertMatch

class MatchRepository(
    val getMatchByIdApiDataSource: GetMatchById,
    val getMatchByIdDBDataSource: GetMatchById,
    val insertMatchDBDataSource: InsertMatch
) :
    GetMatchById,
    InsertMatch {

    override fun getMatchById(id: String): Either<AbsError, Match> {
        val dbResult = getMatchByIdDBDataSource.getMatchById(id)

        if (dbResult is Success) return dbResult

        val apiResult = getMatchByIdApiDataSource.getMatchById(id)

        return apiResult
    }

    override fun insertMatch(match: Match) {
        if (match.placeForCurrentPlayer > 0) // Workaround to avoid inserting matches twice
            insertMatchDBDataSource.insertMatch(match)
    }
}
