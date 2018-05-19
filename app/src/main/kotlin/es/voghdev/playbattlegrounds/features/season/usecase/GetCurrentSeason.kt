package es.voghdev.playbattlegrounds.features.season.usecase

import arrow.core.Either
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.features.season.Season

interface GetCurrentSeason {
    fun getCurrentSeason(): Either<AbsError, Season>
}
