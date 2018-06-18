package es.voghdev.playbattlegrounds.features.players.sharedpreference

import android.content.Context
import arrow.core.Either
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.features.players.model.Player
import es.voghdev.playbattlegrounds.features.players.usecase.IsContentAvailableForPlayer
import es.voghdev.playbattlegrounds.getIntPreference
import es.voghdev.playbattlegrounds.putPreference

class IsContentAvailableSharedPrefDataSource(val appContext: Context?) : IsContentAvailableForPlayer {
    val REQUEST_COUNTER = "requestCounter"

    override fun isContentAvailableForPlayer(player: Player): Either<AbsError, Boolean> {
        val count = appContext?.getIntPreference(REQUEST_COUNTER) ?: 0

        if (count == 3) {
            appContext?.putPreference(REQUEST_COUNTER, 0)
            return Either.right(true)
        } else {
            appContext?.putPreference(REQUEST_COUNTER, count.plus(1))
        }

        return Either.right(false)
    }
}
