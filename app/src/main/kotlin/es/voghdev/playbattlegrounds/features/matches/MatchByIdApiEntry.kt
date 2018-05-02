package es.voghdev.playbattlegrounds.features.matches

class MatchByIdApiEntry(
        val type: String,
        val id: String,
        val attributes: MatchAttributesApiEntry?,
        val relationships: MatchRelationshipsApiEntry,
        val included: List<ParticipantByIdApiEntry>
)
