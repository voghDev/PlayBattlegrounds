package es.voghdev.playbattlegrounds.season1.features.tops

import arrow.core.Either
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.season1.features.tops.model.TopPlayer

interface GetTopPlayers {
    fun getTopPlayers(): Either<AbsError, List<TopPlayer>>
}
