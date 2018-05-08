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
package es.voghdev.playbattlegrounds.features.onboarding.datasource.sharedpreference

import android.content.Context
import arrow.core.Either
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.features.onboarding.usecase.GetPlayerAccount
import es.voghdev.playbattlegrounds.features.onboarding.usecase.SetPlayerAccount
import es.voghdev.playbattlegrounds.getStringPreference
import es.voghdev.playbattlegrounds.putPreference

class PlayerAccountPreferences(val appContext: Context) : GetPlayerAccount, SetPlayerAccount {
    val PLAYER_ACCOUNT = "player_account"

    override fun setPlayerAccount(name: String) =
            appContext.putPreference(PLAYER_ACCOUNT, name)

    override fun getPlayerAccount(): Either<AbsError, String> =
            Either.Right(appContext.getStringPreference(PLAYER_ACCOUNT))
}