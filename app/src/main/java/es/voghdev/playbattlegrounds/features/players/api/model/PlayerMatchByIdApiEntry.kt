package es.voghdev.playbattlegrounds.features.players.api.model

import es.voghdev.playbattlegrounds.features.matches.Match

class PlayerMatchByIdApiEntry(
    val type: String,
    val id: String
) {
    fun toDomain(): Match {
        return Match(id = id)
    }
}
