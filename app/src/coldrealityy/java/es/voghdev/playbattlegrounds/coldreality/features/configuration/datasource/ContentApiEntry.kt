package es.voghdev.playbattlegrounds.coldreality.features.configuration.datasource

import es.voghdev.playbattlegrounds.features.players.model.Content

class ContentApiEntry(
    val id: Long,
    val title: String,
    val text: String,
    val imageUrl: String,
    val embeddedVideoUrl: String,
    val buttonText: String,
    val link: String,
    val type: String,
    val language: String
) {
    fun toDomain(): Content = Content(
        id,
        title,
        text,
        imageUrl,
        embeddedVideoUrl,
        buttonText,
        link
    )
}
