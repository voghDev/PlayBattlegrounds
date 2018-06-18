package es.voghdev.playbattlegrounds.features.players.ui.presenter

import com.appandweb.weevento.ui.presenter.Presenter
import es.voghdev.playbattlegrounds.common.reslocator.ResLocator
import es.voghdev.playbattlegrounds.features.players.usecase.GetContentById

class ContentDetailPresenter(val resLocator: ResLocator, val getContentById: GetContentById) :
        Presenter<ContentDetailPresenter.MVPView, ContentDetailPresenter.Navigator>() {

    suspend override fun initialize() = Unit

    suspend fun onInitialData(data: InitialData) = Unit

    interface MVPView {
        fun showContentTitle(title: String)
        fun showContentText(text: String)
        fun showButtonText(text: String)
    }

    interface Navigator {
        fun close()
    }

    interface InitialData {
        fun getContentId(): Long
    }
}
