package es.voghdev.playbattlegrounds.coldreality.features.configuration

import es.voghdev.playbattlegrounds.coldreality.features.configuration.usecase.GetConfiguration
import es.voghdev.playbattlegrounds.common.Presenter
import es.voghdev.playbattlegrounds.common.reslocator.ResLocator
import es.voghdev.playbattlegrounds.features.players.PlayerRepository
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

class ConfigurationPresenter(val resLocator: ResLocator, val getConfiguration: GetConfiguration, val playerRepository: PlayerRepository) :
    Presenter<ConfigurationPresenter.MVPView, ConfigurationPresenter.Navigator>() {

    override suspend fun initialize() {
        async(CommonPool) {
            getConfiguration.getConfiguration().fold({}) {
                it.contents?.forEach {
                    playerRepository.insertContent(it)
                }
            }
        }.await()

        navigator?.launchOnboardingScreen()
        navigator?.close()
    }

    interface MVPView

    interface Navigator {
        fun launchOnboardingScreen()
        fun close()
    }
}
