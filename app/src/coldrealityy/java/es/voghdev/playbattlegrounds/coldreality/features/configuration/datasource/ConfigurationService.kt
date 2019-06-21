package es.voghdev.playbattlegrounds.coldreality.features.configuration.datasource

import retrofit2.Call
import retrofit2.http.GET

interface ConfigurationService {
    @GET("persons/mobile/configuration-plain")
    fun getConfiguration(): Call<ConfigurationApiResponse>
}
