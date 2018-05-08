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
import es.voghdev.playbattlegrounds.features.onboarding.model.Region
import es.voghdev.playbattlegrounds.features.onboarding.usecase.GetPlayerRegion
import es.voghdev.playbattlegrounds.features.onboarding.usecase.SetPlayerRegion
import es.voghdev.playbattlegrounds.getStringPreference
import es.voghdev.playbattlegrounds.putPreference

class PlayerRegionPreferences(val appContext: Context) : SetPlayerRegion, GetPlayerRegion {
    val PLAYER_REGION = "player_region"

    override fun setCurrentRegion(region: Region) =
            appContext.putPreference(PLAYER_REGION, region.name)

    override fun getPlayerRegion(): Either<AbsError, Region> =
            Either.right(Region(appContext.getStringPreference(PLAYER_REGION)))
}
