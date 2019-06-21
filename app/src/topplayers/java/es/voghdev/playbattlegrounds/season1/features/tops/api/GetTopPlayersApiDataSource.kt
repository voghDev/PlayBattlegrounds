package es.voghdev.playbattlegrounds.season1.features.tops.api

import arrow.core.Either
import arrow.core.Try
import arrow.core.getOrElse
import es.voghdev.playbattlegrounds.BuildConfig
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.common.api.LogJsonInterceptor
import es.voghdev.playbattlegrounds.season1.features.tops.GetTopPlayers
import es.voghdev.playbattlegrounds.season1.features.tops.model.TopPlayer
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.UnknownHostException

class GetTopPlayersApiDataSource : GetTopPlayers, ApiRequest() {
    override fun getTopPlayers(): Either<AbsError, List<TopPlayer>> {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG)
            builder.addInterceptor(LogJsonInterceptor())

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(endPoint)
            .addConverterFactory(GsonConverterFactory.create())
            .client(builder.build())
            .build()

        val service: TopPlayerService = retrofit.create(TopPlayerService::class.java)

        val call: Call<TopPlayersApiResponse> = service.getTopPlayers()

        val request = Try {
            val rsp: Response<TopPlayersApiResponse>? = call.execute()

            if (rsp?.errorBody() != null)
                throw Exception(rsp?.errorBody()?.string())

            return Either.right(rsp?.body()?.toDomain() ?: emptyList<TopPlayer>())
        }

        return request.getOrElse {
            Either.left(AbsError(
                if (it is UnknownHostException)
                    "Please check your Internet connection"
                else it.message ?: "Unknown Error"))
        }
    }
}
