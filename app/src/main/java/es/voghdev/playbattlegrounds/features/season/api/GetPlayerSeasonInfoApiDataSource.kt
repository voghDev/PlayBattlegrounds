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
package es.voghdev.playbattlegrounds.features.season.api

import arrow.core.Either
import com.google.gson.JsonSyntaxException
import es.voghdev.playbattlegrounds.BuildConfig
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.common.api.ApiRequest
import es.voghdev.playbattlegrounds.common.api.AuthInterceptor
import es.voghdev.playbattlegrounds.common.api.LogJsonInterceptor
import es.voghdev.playbattlegrounds.features.players.model.Player
import es.voghdev.playbattlegrounds.features.season.Season
import es.voghdev.playbattlegrounds.features.season.api.model.SeasonInfoApiResponse
import es.voghdev.playbattlegrounds.features.season.model.PlayerSeasonGameModeStats
import es.voghdev.playbattlegrounds.features.season.model.PlayerSeasonInfo
import es.voghdev.playbattlegrounds.features.season.usecase.GetPlayerSeasonInfo
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class GetPlayerSeasonInfoApiDataSource : GetPlayerSeasonInfo, ApiRequest {
    override fun getPlayerSeasonInfo(player: Player, season: Season): Either<AbsError, PlayerSeasonInfo> {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG)
            builder.addInterceptor(LogJsonInterceptor())

        builder.addNetworkInterceptor(AuthInterceptor(BuildConfig.PUBGApiKey))
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(getEndPoint())
            .addConverterFactory(GsonConverterFactory.create())
            .client(builder.build())
            .build()

        val service: SeasonService = retrofit.create(SeasonService::class.java)

        val call: Call<SeasonInfoApiResponse> = service.getPlayerSeasonInfo(
            "Bearer ${BuildConfig.PUBGApiKey}",
            "application/vnd.api+json",
            getDefaultRegion(),
            player.id,
            season.id)

        try {
            val rsp: Response<SeasonInfoApiResponse>? = call.execute()

            if (rsp?.body()?.hasData() == true) {
                return Either.right(rsp?.body()?.toDomain() ?: emptySeasonInfo())
            } else if (rsp?.errorBody() != null) {
                val error = rsp?.errorBody()?.string()!!
                return Either.left(AbsError(error))
            }
        } catch (e: JsonSyntaxException) {
            return Either.left(AbsError(e.message ?: "Unknown error parsing JSON"))
        } catch (e: UnknownHostException) {
            return Either.left(AbsError("Please check your internet connection"))
        }

        return Either.left(AbsError("Unknown error fetching PlayerSeasonInfo"))
    }

    private fun emptySeasonInfo() = PlayerSeasonInfo(
        PlayerSeasonGameModeStats(),
        PlayerSeasonGameModeStats(),
        PlayerSeasonGameModeStats(),
        PlayerSeasonGameModeStats(),
        PlayerSeasonGameModeStats(),
        PlayerSeasonGameModeStats()
    )
}
