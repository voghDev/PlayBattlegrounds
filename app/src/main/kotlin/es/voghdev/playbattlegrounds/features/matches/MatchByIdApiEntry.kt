package es.voghdev.playbattlegrounds.features.matches

import es.voghdev.playbattlegrounds.features.matchesv1.ParticipantApiEntry

class MatchByIdApiEntry(
        val type: String,
        val id: String,
        val attributes: MatchAttributesApiEntry?,
        val relationships: MatchRelationshipsApiEntry,
        val included: List<ParticipantApiEntry>
)
