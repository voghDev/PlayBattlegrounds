package es.voghdev.playbattlegrounds.features.matches.api

class MatchByIdApiEntry(
        val type: String,
        val id: String,
        val attributes: MatchAttributesApiEntry?,
        val relationships: MatchRelationshipsApiEntry,
        val included: List<ParticipantByIdApiEntry>
)
