package es.voghdev.playbattlegrounds.season1.features.tops.model

import es.voghdev.playbattlegrounds.common.ui.ListEntity
import es.voghdev.playbattlegrounds.features.players.model.Player

data class TopPlayer(val player: Player) : ListEntity() {
    var picture: String = ""
    var bio: String = ""
    var country: String = ""
    var flag: String = ""
    var position: Int = 0

    fun getRemoteUrl(): String = picture

    fun getFlagRemoteUrl(): String = flag
}
