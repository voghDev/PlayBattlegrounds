package es.voghdev.playbattlegrounds.features.players.db

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import es.voghdev.playbattlegrounds.common.db.AppDatabase
import es.voghdev.playbattlegrounds.features.players.db.ContentDBEntry.Companion.NAME
import es.voghdev.playbattlegrounds.features.players.model.Content

@Table(name = NAME, database = AppDatabase::class, allFields = true)
class ContentDBEntry : BaseModel {
    companion object {
        const val NAME = "ContentDBEntry"
    }

    @PrimaryKey
    @Column
    var id: Long = 0L
    @Column
    var title: String = ""
    @Column
    var text: String = ""
    @Column
    var imageUrl: String = ""
    @Column
    var embeddedVideoUrl: String = ""
    @Column
    var buttonText: String = ""
    @Column
    var link: String = ""
    @Column
    var type: String = ""

    constructor() {
        // Mandatory for DBFlow
    }

    constructor(content: Content) {
        id = content.id
        title = content.title
        text = content.text
        imageUrl = content.imageUrl
        embeddedVideoUrl = content.embeddedVideoUrl
        buttonText = content.buttonText
        link = content.link
        type = content.type
    }

    fun toDomain(): Content =
        Content(
            id,
            title,
            text,
            imageUrl,
            embeddedVideoUrl,
            buttonText,
            link,
            type
        )
}
