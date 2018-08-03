package es.voghdev.playbattlegrounds.features.matches.api.model

import es.voghdev.playbattlegrounds.features.matches.Match

class MatchByIdApiResponse(
    val data: MatchByIdApiEntry?,
    val included: List<ParticipantByIdApiEntry>?
) {
    fun hasData(): Boolean = data?.id?.isNotEmpty() == true

    fun toDomain(): Match {
        return data?.toDomain()?.apply {
            participants = included?.map { it.toDomain() } ?: emptyList()
        } ?: Match()
    }
}
