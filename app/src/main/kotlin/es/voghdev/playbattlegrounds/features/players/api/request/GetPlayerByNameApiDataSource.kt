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
package es.voghdev.playbattlegrounds.features.players.api.request

import arrow.core.Either
import com.google.gson.JsonSyntaxException
import es.voghdev.playbattlegrounds.BuildConfig
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.common.api.LogJsonInterceptor
import es.voghdev.playbattlegrounds.datasource.api.ApiRequest
import es.voghdev.playbattlegrounds.datasource.api.model.PlayerService
import es.voghdev.playbattlegrounds.features.players.api.model.PlayerByIdApiResponse
import es.voghdev.playbattlegrounds.features.players.model.Player
import es.voghdev.playbattlegrounds.features.players.usecase.GetPlayerByName
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetPlayerByNameApiDataSource : GetPlayerByName, ApiRequest {
    override fun getPlayerByName(name: String): Either<AbsError, Player> {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG)
            builder.addInterceptor(LogJsonInterceptor())

        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(getEndPoint())
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build()

        val service: PlayerService = retrofit.create(PlayerService::class.java)

        val call: Call<PlayerByIdApiResponse> = service.getPlayerByName(
                "Bearer ${BuildConfig.PUBGApiKey}",
                "application/vnd.api+json",
                name
        )

        try {
            val rsp: Response<PlayerByIdApiResponse>? = call.execute()

            if (rsp?.body()?.hasData() == true) {
                return Either.right(rsp?.body()?.data?.first()?.toDomain()!!)
            } else if (rsp?.errorBody() != null) {
                val error = rsp?.errorBody()?.string()!!
                return Either.left(AbsError(error))
            }
        } catch (e: JsonSyntaxException) {
            return Either.left(AbsError(e.message ?: "Unknown error parsing JSON"))
        }

        return Either.left(AbsError("Unknown error fetching player"))
    }
}