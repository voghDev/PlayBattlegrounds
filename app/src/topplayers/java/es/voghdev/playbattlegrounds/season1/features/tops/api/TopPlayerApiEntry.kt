package es.voghdev.playbattlegrounds.season1.features.tops.api

import es.voghdev.playbattlegrounds.features.players.model.Player
import es.voghdev.playbattlegrounds.season1.features.tops.model.TopPlayer

class TopPlayerApiEntry(
    val id: String,
    val name: String,
    val bio: String,
    val image: String,
    val country: String,
    val flag: String,
    val server: String,
    val position: Int
) {
    fun toDomain(): TopPlayer {
        val topPlayer = TopPlayer(Player(
            id,
            name,
            "",
            "",
            "",
            server,
            emptyList()
        )).apply {
            this.picture = image
        }

        topPlayer.country = country
        topPlayer.flag = flag
        topPlayer.bio = bio
        topPlayer.position = position

        return topPlayer
    }
}
