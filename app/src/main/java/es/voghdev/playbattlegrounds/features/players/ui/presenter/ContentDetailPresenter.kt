package es.voghdev.playbattlegrounds.features.players.ui.presenter

import es.voghdev.playbattlegrounds.common.Presenter
import es.voghdev.playbattlegrounds.common.Success
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

        if (result is Success) {
            content = result.b

            view?.render(ViewState.Success(
                content.title,
                content.text,
                content.isButtonVisible,
                content.buttonText
            ))
        }
    }

    fun onLinkButtonClicked() {
        navigator?.launchBrowser(content.link)
    }

    sealed class ViewState {
        object Failure : ViewState()
        object Loading : ViewState()
        data class Success(
            val contentTitle: String,
            val contentText: String,
            val isButtonVisible: Boolean,
            val buttonText: String
        ) : ViewState()
    }

    interface MVPView {
        fun render(state: ViewState)

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
