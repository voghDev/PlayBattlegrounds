package es.voghdev.playbattlegrounds.features.players.usecase

import arrow.core.Either
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.features.players.model.Player

interface IsContentAvailableForPlayer {
    fun isContentAvailableForPlayer(player: Player): Either<AbsError, Boolean>
}
