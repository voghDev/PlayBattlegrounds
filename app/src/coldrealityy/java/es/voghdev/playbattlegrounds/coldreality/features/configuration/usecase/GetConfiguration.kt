package es.voghdev.playbattlegrounds.coldreality.features.configuration.usecase

import arrow.core.Either
import es.voghdev.playbattlegrounds.coldreality.features.configuration.model.CRConfiguration
import es.voghdev.playbattlegrounds.common.AbsError

interface GetConfiguration {
    fun getConfiguration(): Either<AbsError, CRConfiguration>
}
