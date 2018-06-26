package es.voghdev.playbattlegrounds.features.matches.api

import es.voghdev.playbattlegrounds.features.matches.api.model.MatchByIdApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface MatchService {
    @GET("shards/{region}/matches/{id}")
    fun getMatchById(
            @Header("Authorization") apiKey: String,
            @Header("accept") mediaType: String,
            @Path("region") region: String,
            @Path("id") matchId: String): Call<MatchByIdApiResponse>
}