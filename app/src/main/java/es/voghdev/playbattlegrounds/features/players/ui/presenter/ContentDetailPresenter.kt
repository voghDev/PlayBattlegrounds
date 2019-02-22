package es.voghdev.playbattlegrounds.features.players.ui.presenter

import arrow.core.Either
import es.voghdev.playbattlegrounds.common.Presenter
import es.voghdev.playbattlegrounds.features.players.PlayerRepository
import es.voghdev.playbattlegrounds.features.players.model.Content

class ContentDetailPresenter(val playerRepository: PlayerRepository) :
    Presenter<ContentDetailPresenter.MVPView, ContentDetailPresenter.Navigator>() {

    var content: Content = Content()

    override fun initialize() {
        view?.configureToolbar()
    }

    fun onInitialData(data: InitialData) {
        val result = playerRepository.getContentById(data.getContentId())

        if (result is Either.Right) {
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
