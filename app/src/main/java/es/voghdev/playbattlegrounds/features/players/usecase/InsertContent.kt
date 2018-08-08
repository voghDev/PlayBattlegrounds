package es.voghdev.playbattlegrounds.features.players.usecase

import arrow.core.Either
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.features.players.model.Content

interface InsertContent {
    fun insertContent(content: Content): Either<AbsError, Boolean>
}
