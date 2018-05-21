package es.voghdev.playbattlegrounds.features.players.db

import arrow.core.Either
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.features.players.model.Player
import es.voghdev.playbattlegrounds.features.players.usecase.InsertPlayer

class InsertPlayerDBImpl : InsertPlayer {
    override fun insertPlayer(player: Player): Either<AbsError, Boolean> =
            Either.right(PlayerDBEntry(player).save())

}
