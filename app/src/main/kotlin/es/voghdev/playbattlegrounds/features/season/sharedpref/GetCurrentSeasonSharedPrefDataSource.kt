package es.voghdev.playbattlegrounds.features.season.sharedpref

import android.content.Context
import arrow.core.Either
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.features.season.Season
import es.voghdev.playbattlegrounds.features.season.usecase.GetCurrentSeason
import es.voghdev.playbattlegrounds.getStringPreference

class GetCurrentSeasonSharedPrefDataSource(val appContext: Context) : GetCurrentSeason {
    override fun getCurrentSeason(): Either<AbsError, Season> {
        val id = appContext.getStringPreference("currentSeason")

        return Either.right(Season(id, isCurrentSeason = true, isOffSeason = false))
    }
}
