package es.voghdev.playbattlegrounds.features.players.ui.presenter

import es.voghdev.playbattlegrounds.common.Ok
import es.voghdev.playbattlegrounds.common.Presenter
import es.voghdev.playbattlegrounds.features.players.PlayerRepository
import es.voghdev.playbattlegrounds.features.players.model.Content
import kotlinx.coroutines.CoroutineDispatcher

class ContentDetailPresenter(
    dispatcher: CoroutineDispatcher,
    val playerRepository: PlayerRepository
) : Presenter<ContentDetailPresenter.MVPView, ContentDetailPresenter.Navigator>(dispatcher) {

    var content: Content = Content()

    override suspend fun initialize() {
        view?.configureToolbar()
    }

    suspend fun onInitialData(data: InitialData) {
        val result = playerRepository.getContentById(data.getContentId())

        if (result is Ok) {
            content = result.b
            view?.showContentTitle(content.title)
            view?.showContentText(content.text)

            if (content.isButtonVisible)
                view?.showButtonText(content.buttonText)
        }
    }

    fun onLinkButtonClicked() {
        navigator?.launchBrowser(content.link)
    }

    interface MVPView {
        fun showContentTitle(title: String)
        fun showContentText(text: String)
        fun showButtonText(text: String)
        fun configureToolbar()
    }

    interface Navigator {
        fun close()
        fun launchBrowser(link: String)
    }

    interface InitialData {
        fun getContentId(): Long
    }
}
