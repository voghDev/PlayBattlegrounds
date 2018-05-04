package es.voghdev.playbattlegrounds.features.matches.api.model

import es.voghdev.playbattlegrounds.features.matches.Match
import es.voghdev.playbattlegrounds.toDate

class MatchByIdApiEntry(
        val type: String,
        val id: String,
        val attributes: MatchAttributesApiEntry?,
        val relationships: MatchRelationshipsApiEntry,
        val included: List<ParticipantByIdApiEntry>
) {
    fun toDomain(): Match {
        return Match(
                id = id,
                date = attributes?.createdAt?.toDate("yyyy-MM-dd") ?: 0L,
                gameMode = attributes?.gameMode ?: "",
                map = attributes?.mapName ?: "",
                durationInSeconds = attributes?.duration ?: 0
        )
    }
}