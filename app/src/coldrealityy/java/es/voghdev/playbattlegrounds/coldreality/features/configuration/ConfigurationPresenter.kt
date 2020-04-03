package es.voghdev.playbattlegrounds.coldreality.features.configuration

import es.voghdev.playbattlegrounds.coldreality.features.configuration.usecase.GetConfiguration
import es.voghdev.playbattlegrounds.common.Presenter
import es.voghdev.playbattlegrounds.features.players.PlayerRepository
import es.voghdev.playbattlegrounds.log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ConfigurationPresenter(
    dispatcher: CoroutineDispatcher,
    private val getConfiguration: GetConfiguration,
    val playerRepository: PlayerRepository
) : Presenter<ConfigurationPresenter.MVPView, ConfigurationPresenter.Navigator>(dispatcher) {

    override suspend fun initialize() {
        withContext(dispatcher) {
            getConfiguration.getConfiguration()
                .fold(ifLeft = ::log, ifRight = { response ->
                    response.contents.forEach {
                        playerRepository.insertContent(it)
                    }
                })
        }

        navigator?.launchOnboardingScreen()
        navigator?.close()
    }

    interface MVPView

    interface Navigator {
        fun launchOnboardingScreen()
        fun close()
    }
}
