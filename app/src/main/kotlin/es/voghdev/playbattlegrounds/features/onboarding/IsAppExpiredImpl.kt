package es.voghdev.playbattlegrounds.features.onboarding

import es.voghdev.playbattlegrounds.features.onboarding.usecase.IsAppExpired

class IsAppExpiredImpl(val now: Long, val limit: Long) : IsAppExpired {
    override fun isAppExpired(): Boolean = (limit in 1..(now - 1))
}
