package es.voghdev.playbattlegrounds.features.players

import arrow.core.Either
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.features.players.model.Player
import es.voghdev.playbattlegrounds.features.players.usecase.GetPlayerById
import es.voghdev.playbattlegrounds.features.players.usecase.GetPlayerByName

class PlayerRepository(
        val getPlayerByIdApiDataSource: GetPlayerById,
        val getPlayerByNameApiDataSource: GetPlayerByName,
        val tooManyRequestsError: String
) :
        GetPlayerByName,
        GetPlayerById by getPlayerByIdApiDataSource {

    var start = 0L
    var count = 0

    override fun getPlayerByName(name: String, region: String): Either<AbsError, Player> {
        val result = getPlayerByNameApiDataSource.getPlayerByName(name, region)

        return if (userCanRequest(System.currentTimeMillis())) result
        else Either.left(AbsError(tooManyRequestsError))
    }

    fun userCanRequest(ms: Long): Boolean {
        /*
        * Bluehole has announced the traffic limit in the /matches request has been removed,
        * So no more throttling, Yeehaa!
        * */
        return true
    }
}
