package es.voghdev.playbattlegrounds.features.matches.api.model

class MatchByIdApiResponse(val data: MatchByIdApiEntry?) {
    fun hasData(): Boolean = data?.id?.isNotEmpty() == true
}
