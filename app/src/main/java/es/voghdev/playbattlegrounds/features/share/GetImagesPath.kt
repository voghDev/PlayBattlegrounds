package es.voghdev.playbattlegrounds.features.share

import arrow.core.Either
import es.voghdev.playbattlegrounds.common.AbsError

interface GetImagesPath {
    fun getImagesPath(): Either<AbsError, String>
}
