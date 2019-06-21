package es.voghdev.playbattlegrounds.coldreality.features.configuration.datasource

import es.voghdev.playbattlegrounds.coldreality.features.configuration.model.CRConfiguration

class ConfigurationApiResponse(val tiers: List<ContentApiEntry>?) {
    fun toDomain(): CRConfiguration =
        CRConfiguration(tiers?.map { it.toDomain() } ?: emptyList())
}
