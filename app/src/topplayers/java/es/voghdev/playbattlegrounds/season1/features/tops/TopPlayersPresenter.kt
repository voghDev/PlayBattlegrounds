package es.voghdev.playbattlegrounds.season1.features.tops

import es.voghdev.playbattlegrounds.common.Presenter
import es.voghdev.playbattlegrounds.common.reslocator.ResLocator
import es.voghdev.playbattlegrounds.features.season.usecase.GetSeasons
import es.voghdev.playbattlegrounds.features.season.usecase.SetCurrentSeason
import es.voghdev.playbattlegrounds.season1.common.asset.CopyAsset
import es.voghdev.playbattlegrounds.season1.features.tops.model.TopPlayer
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

class TopPlayersPresenter(val resLocator: ResLocator, val getTopPlayers: GetTopPlayers, val copyAsset: CopyAsset, val getPicturesPath: GetPicturesPath, val getSeasons: GetSeasons, val setCurrentSeason: SetCurrentSeason) :
        Presenter<TopPlayersPresenter.MVPView, TopPlayersPresenter.Navigator>() {

    override suspend fun initialize() {

        storeCurrentSeason()
        async(CommonPool) {
            getTopPlayers.getTopPlayers()
        }
                .await()
                .fold({}, { players ->
                    players.sortedBy { it.position }.forEach { player ->
                        view?.addPlayer(player)
                    }
                })
    }

    suspend fun storeCurrentSeason() {
        async(CommonPool) {
            getSeasons.getSeasons()
        }.await().fold({}, {
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