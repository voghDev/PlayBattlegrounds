package es.voghdev.playbattlegrounds.features.players.api.model

class PlayerAttributesApiEntry(
    val createdAt: String,
    val name: String,
    val patchVersion: String,
    val shardId: String,
    val stats: Any?,
    val titleId: String,
    val updatedAt: String
)
