package es.voghdev.playbattlegrounds.features.matches.api.model

import es.voghdev.playbattlegrounds.features.matches.Match
import es.voghdev.playbattlegrounds.toDate

class MatchByIdApiEntry(
        val type: String,
        val id: String,
        val attributes: MatchAttributesApiEntry?,
        val relationships: MatchRelationshipsApiEntry
) {
    fun toDomain(): Match = Match(
            id = id,
            date = attributes?.createdAt?.toDate("yyyy-MM-dd HH:mm:ss") ?: 0L,
            gameMode = attributes?.gameMode ?: "",
            map = attributes?.mapName ?: "",
            durationInSeconds = attributes?.duration ?: 0
    )
}