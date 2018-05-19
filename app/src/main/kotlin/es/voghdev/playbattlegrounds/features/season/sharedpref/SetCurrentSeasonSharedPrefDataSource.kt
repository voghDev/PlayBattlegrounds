package es.voghdev.playbattlegrounds.features.season.sharedpref

import android.content.Context
import es.voghdev.playbattlegrounds.features.season.Season
import es.voghdev.playbattlegrounds.features.season.usecase.SetCurrentSeason
import es.voghdev.playbattlegrounds.putPreference

class SetCurrentSeasonSharedPrefDataSource(val appContext: Context) : SetCurrentSeason {
    override fun setCurrentSeason(season: Season) {
        appContext.putPreference("currentSeason", season.id)
    }
}
