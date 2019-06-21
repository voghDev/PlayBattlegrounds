package es.voghdev.playbattlegrounds.season1.common

import android.content.Context
import arrow.core.Either
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.season1.features.tops.GetPicturesPath

class GetPicturesPathAndroidImpl(val context: Context) : GetPicturesPath {
    override fun getPicturesPath(): Either<AbsError, String> =
        Either.right(context.getExternalFilesDir("img").absolutePath)
}