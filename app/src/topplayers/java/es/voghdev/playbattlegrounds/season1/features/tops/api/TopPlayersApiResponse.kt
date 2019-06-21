package es.voghdev.playbattlegrounds.season1.features.tops.api

import es.voghdev.playbattlegrounds.season1.features.tops.model.TopPlayer

class TopPlayersApiResponse(val gamers: List<TopPlayerApiEntry>?) {
    fun toDomain(): List<TopPlayer> =
        gamers?.map { it.toDomain() } ?: emptyList()
}