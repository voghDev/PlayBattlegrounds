package es.voghdev.playbattlegrounds.coldreality.features.configuration.datasource

import arrow.core.Either
import arrow.core.Try
import arrow.core.getOrElse
import es.voghdev.playbattlegrounds.BuildConfig
import es.voghdev.playbattlegrounds.coldreality.features.configuration.model.CRConfiguration
import es.voghdev.playbattlegrounds.coldreality.features.configuration.usecase.GetConfiguration
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.common.api.LogJsonInterceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.UnknownHostException

class GetConfigurationApiDataSource : GetConfiguration, ApiRequest() {
    override fun getConfiguration(): Either<AbsError, CRConfiguration> {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG)
            builder.addInterceptor(LogJsonInterceptor())

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(endPoint)
            .addConverterFactory(GsonConverterFactory.create())
            .client(builder.build())
            .build()

        val service: ConfigurationService = retrofit.create(ConfigurationService::class.java)

        val call: Call<ConfigurationApiResponse> = service.getConfiguration()

        val request = Try {
            val rsp: Response<ConfigurationApiResponse>? = call.execute()

            if (rsp?.errorBody() != null)
                throw Exception(rsp?.errorBody()?.string())

            return Either.right(rsp?.body()?.toDomain() ?: CRConfiguration(emptyList()))
        }

        return request.getOrElse {
            Either.left(AbsError(
                if (it is UnknownHostException)
                    "Please check your Internet connection"
                else it.message ?: "Unknown Error"))
        }
    }
}
