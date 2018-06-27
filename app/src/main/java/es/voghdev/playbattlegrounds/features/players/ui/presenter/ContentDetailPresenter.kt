package es.voghdev.playbattlegrounds.features.players.ui.presenter

import com.appandweb.weevento.ui.presenter.Presenter
import es.voghdev.playbattlegrounds.common.Ok
import es.voghdev.playbattlegrounds.features.players.model.Content
import es.voghdev.playbattlegrounds.features.players.usecase.GetContentById

class ContentDetailPresenter(val getContentById: GetContentById) :
        Presenter<ContentDetailPresenter.MVPView, ContentDetailPresenter.Navigator>() {

    var content: Content = Content()

    suspend override fun initialize() {
        view?.configureToolbar()
    }

    suspend fun onInitialData(data: InitialData) {
        val result = getContentById.getContentById(data.getContentId())

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
