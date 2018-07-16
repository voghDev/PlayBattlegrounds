package es.voghdev.playbattlegrounds.features.share

import android.content.Context
import arrow.core.Either
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.common.App

class GetImagesPathAndroidDataSource(val context: Context) : GetImagesPath {
    override fun getImagesPath(): Either<AbsError, String> {
        val filesDir = (context as? App)?.getExternalFilesDir("img")?.absolutePath ?: ""
        return if (filesDir.isNotEmpty())
            Either.right(filesDir)
        else Either.left(AbsError("Images directory not found"))
    }
}
