package es.voghdev.playbattlegrounds.features.players.db

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import es.voghdev.playbattlegrounds.common.db.AppDatabase
import es.voghdev.playbattlegrounds.features.players.db.PlayerDBEntry.Companion.NAME
import es.voghdev.playbattlegrounds.features.players.model.Player

@Table(name = NAME, database = AppDatabase::class, allFields = true)
class PlayerDBEntry : BaseModel {
    companion object {
        const val NAME = "PlayerDBEntry"
    }

    @PrimaryKey
    @Column
    var id: String = ""
    @Column
    var name: String = ""
    @Column
    var patchVersion: String = ""
    @Column
    var titleId: String = ""
    @Column
    var link: String = ""
    @Column
    var shardId: String = ""

    constructor() {
        // Mandatory for DBFlow
    }

    constructor(player: Player) {
        id = player.id
        name = player.name
        patchVersion = player.patchVersion
        titleId = player.titleId
        link = player.link
        shardId = player.shardId
    }

    fun toDomain(): Player {
        return Player(
                id,
                name,
                patchVersion,
                titleId,
                link,
                shardId,
                emptyList()
        )
    }
}
