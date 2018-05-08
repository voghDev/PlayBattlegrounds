package es.voghdev.playbattlegrounds.features.players.model

import es.voghdev.playbattlegrounds.features.matches.Match

data class Player(
        val id: String,
        val name: String,
        val patchVersion: String,
        val titleId: String,
        val link: String,
        var shardId: String,
        val matches: List<Match> = emptyList()
)
