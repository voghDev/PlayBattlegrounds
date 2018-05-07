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
package es.voghdev.playbattlegrounds.features.matches.api

import arrow.core.Either
import com.google.gson.JsonSyntaxException
import es.voghdev.playbattlegrounds.BuildConfig
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.common.api.AuthInterceptor
import es.voghdev.playbattlegrounds.common.api.LogJsonInterceptor
import es.voghdev.playbattlegrounds.datasource.api.ApiRequest
import es.voghdev.playbattlegrounds.features.matches.Match
import es.voghdev.playbattlegrounds.features.matches.api.model.MatchByIdApiResponse
import es.voghdev.playbattlegrounds.features.matches.usecase.GetMatchById
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetMatchByIdApiDataSource : GetMatchById, ApiRequest {
    override fun getMatchById(id: String): Either<AbsError, Match> {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG)
            builder.addInterceptor(LogJsonInterceptor())

        builder.addNetworkInterceptor(AuthInterceptor(BuildConfig.PUBGApiKey))

        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(getEndPoint())
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build()

        val service: MatchService = retrofit.create(MatchService::class.java)

        val call: Call<MatchByIdApiResponse> = service.getMatchById(
                "Bearer ${BuildConfig.PUBGApiKey}",
                "application/vnd.api+json",
                "pc-eu",
                id
        )

        try {
            val rsp: Response<MatchByIdApiResponse>? = call.execute()

            if (rsp?.body()?.hasData() == true) {
                return Either.right(rsp?.body()?.toDomain() ?: Match())
            } else if (rsp?.errorBody() != null) {
                val error = rsp?.errorBody()?.string()!!
                return Either.left(AbsError(error))
            }
        } catch (e: JsonSyntaxException) {
            return Either.left(AbsError(e.message ?: "Unknown error parsing JSON"))
        }

        return Either.left(AbsError("Unknown error fetching match"))
    }
}