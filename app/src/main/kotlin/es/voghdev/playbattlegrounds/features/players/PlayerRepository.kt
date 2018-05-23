package es.voghdev.playbattlegrounds.features.players

import arrow.core.Either
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.features.players.model.Player
import es.voghdev.playbattlegrounds.features.players.usecase.GetPlayerById
import es.voghdev.playbattlegrounds.features.players.usecase.GetPlayerByName
import es.voghdev.playbattlegrounds.minutes

class PlayerRepository(
        val getPlayerByIdApiDataSource: GetPlayerById,
        val getPlayerByNameApiDataSource: GetPlayerByName,
        val tooManyRequestsError: String
) :
        GetPlayerByName,
        GetPlayerById by getPlayerByIdApiDataSource {

    var start = 0L
    var count = 0

    override fun getPlayerByName(name: String): Either<AbsError, Player> {
        val result = getPlayerByNameApiDataSource.getPlayerByName(name)

        return if (userCanRequest(System.currentTimeMillis())) result
        else Either.left(AbsError(tooManyRequestsError))
    }

    fun userCanRequest(ms: Long): Boolean {
        if (start == 0L)
            start = ms

        if (ms > start + 5.minutes()) {
            count = 0
            start = 0
        }

        if (ms < start + 5.minutes() && ++count > 5)
            return false

        return true
    }
}
