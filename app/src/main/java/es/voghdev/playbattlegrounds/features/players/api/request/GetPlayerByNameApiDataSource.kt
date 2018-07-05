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
import arrow.core.Try
import arrow.core.getOrElse
import es.voghdev.playbattlegrounds.BuildConfig
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.common.api.AuthInterceptor
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
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class GetPlayerByNameApiDataSource() : GetPlayerByName, ApiRequest {
    override fun getPlayerByName(name: String, region: String): Either<AbsError, Player> {
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

        val service: PlayerService = retrofit.create(PlayerService::class.java)

        val call: Call<PlayerByIdApiResponse> = service.getPlayerByName(
                "Bearer ${BuildConfig.PUBGApiKey}",
                "application/vnd.api+json",
                region,
                name
        )

        val request = Try {
            val rsp: Response<PlayerByIdApiResponse>? = call.execute()

            if (rsp?.errorBody() != null) {
                throw Exception(rsp?.errorBody()?.string())
            }

            return Either.right(rsp?.body()?.data?.first()?.toDomain()!!)
        }

        return request.getOrElse {
            Either.left(AbsError(
                    if (it is UnknownHostException)
                        "Please check your Internet connection"
                    else it.message ?: "Unknown Error"))
        }
    }
}