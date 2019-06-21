package es.voghdev.playbattlegrounds.season1.features.tops

import arrow.core.Either
import es.voghdev.playbattlegrounds.common.AbsError

interface GetPicturesPath {
    fun getPicturesPath(): Either<AbsError, String>
}
