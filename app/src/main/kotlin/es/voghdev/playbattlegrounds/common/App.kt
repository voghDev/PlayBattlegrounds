/*
 * Copyright (C) 2017 Olmo Gallegos Hernández.
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
package es.voghdev.playbattlegrounds.common

import android.app.Application
import android.content.Context
import es.voghdev.playbattlegrounds.common.reslocator.AndroidResLocator
import es.voghdev.playbattlegrounds.common.reslocator.ResLocator
import es.voghdev.playbattlegrounds.features.matches.MatchRepository
import es.voghdev.playbattlegrounds.features.matches.api.GetMatchByIdApiDataSource
import es.voghdev.playbattlegrounds.features.matches.mock.GetMatchByIdMockDataSource
import es.voghdev.playbattlegrounds.features.matches.mock.InsertMatchMockDataSource
import es.voghdev.playbattlegrounds.features.matches.usecase.GetMatchById
import es.voghdev.playbattlegrounds.features.onboarding.datasource.res.GetRegionsAndroidResDataSource
import es.voghdev.playbattlegrounds.features.onboarding.datasource.sharedpreference.PlayerAccountPreferences
import es.voghdev.playbattlegrounds.features.onboarding.datasource.sharedpreference.PlayerRegionPreferences
import es.voghdev.playbattlegrounds.features.onboarding.usecase.GetPlayerAccount
import es.voghdev.playbattlegrounds.features.onboarding.usecase.GetPlayerRegion
import es.voghdev.playbattlegrounds.features.onboarding.usecase.GetRegions
import es.voghdev.playbattlegrounds.features.onboarding.usecase.SetPlayerAccount
import es.voghdev.playbattlegrounds.features.onboarding.usecase.SetPlayerRegion
import es.voghdev.playbattlegrounds.features.players.api.request.GetPlayerByIdApiDataSource
import es.voghdev.playbattlegrounds.features.players.api.request.GetPlayerByNameApiDataSource
import es.voghdev.playbattlegrounds.features.players.usecase.GetPlayerById
import es.voghdev.playbattlegrounds.features.players.usecase.GetPlayerByName
import es.voghdev.playbattlegrounds.features.season.api.GetSeasonsApiDataSource
import es.voghdev.playbattlegrounds.features.season.usecase.GetSeasons
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class App : Application(), KodeinAware {
    override val kodein = Kodein {
        bind<GetPlayerById>() with singleton { GetPlayerByIdApiDataSource(PlayerRegionPreferences(applicationContext)) }
        bind<GetMatchById>() with singleton { GetMatchByIdApiDataSource(PlayerRegionPreferences(applicationContext)) }
        bind<GetPlayerByName>() with singleton { GetPlayerByNameApiDataSource(PlayerRegionPreferences(applicationContext)) }
        bind<ResLocator>() with singleton { AndroidResLocator(applicationContext) }
        bind<SetPlayerAccount>() with singleton { PlayerAccountPreferences(applicationContext) }
        bind<GetPlayerAccount>() with singleton { PlayerAccountPreferences(applicationContext) }
        bind<GetRegions>() with singleton { GetRegionsAndroidResDataSource(applicationContext) }
        bind<GetSeasons>() with singleton { GetSeasonsApiDataSource() }
        bind<SetPlayerRegion>() with singleton { PlayerRegionPreferences(applicationContext) }
        bind<GetPlayerRegion>() with singleton { PlayerRegionPreferences(applicationContext) }
        bind<MatchRepository>() with singleton {
            MatchRepository(
                    GetMatchByIdApiDataSource(PlayerRegionPreferences(applicationContext)),
                    GetMatchByIdMockDataSource(),
                    InsertMatchMockDataSource()
            )
        }
    }
}

fun Context.asApp(): App = this.applicationContext as App