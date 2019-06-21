package es.voghdev.playbattlegrounds.season1.features.tops.api

import retrofit2.Call
import retrofit2.http.GET

interface TopPlayerService {
    @GET("gamers/pubg")
    fun getTopPlayers(): Call<TopPlayersApiResponse>
}