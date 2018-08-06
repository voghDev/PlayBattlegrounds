package es.voghdev.playbattlegrounds.features.players.model

class Content(
    val id: Long = 0L,
    val title: String = "",
    val text: String = "",
    val imageUrl: String = "",
    val embeddedVideoUrl: String = "",
    val buttonText: String = "",
    val type: String = "",
    val link: String = ""
) {
    val isButtonVisible: Boolean = link.isNotEmpty()
}
