package es.voghdev.playbattlegrounds.season1.features.tops

import es.voghdev.playbattlegrounds.common.Presenter
import es.voghdev.playbattlegrounds.features.season.usecase.GetSeasons
import es.voghdev.playbattlegrounds.features.season.usecase.SetCurrentSeason
import es.voghdev.playbattlegrounds.season1.features.tops.model.TopPlayer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class TopPlayersPresenter(
    dispatcher: CoroutineDispatcher,
    val getTopPlayers: GetTopPlayers,
    val getSeasons: GetSeasons,
    val setCurrentSeason: SetCurrentSeason
) : Presenter<TopPlayersPresenter.MVPView, TopPlayersPresenter.Navigator>(dispatcher) {

    override suspend fun initialize() {

        storeCurrentSeason()
        withContext(dispatcher) {
            getTopPlayers.getTopPlayers()
        }
                .fold({}, { players ->
                    players.sortedBy { it.position }.forEach { player ->
                        view?.addPlayer(player)
                    }
                })
    }

    suspend fun storeCurrentSeason() {
        withContext(dispatcher) {
            getSeasons.getSeasons()
        }.fold({}, {
            val currentSeason = it.firstOrNull { it.isCurrentSeason }
            if (currentSeason != null)
                setCurrentSeason.setCurrentSeason(currentSeason)
        })
    }

    fun onTopPlayerClicked(topPlayer: TopPlayer) {
        navigator?.launchPlayerSearchScreenForPlayer(topPlayer)
    }

    interface MVPView {
        fun addPlayer(player: TopPlayer)
    }

    interface Navigator {
        fun launchPlayerSearchScreenForPlayer(topPlayer: TopPlayer)
    }
}
