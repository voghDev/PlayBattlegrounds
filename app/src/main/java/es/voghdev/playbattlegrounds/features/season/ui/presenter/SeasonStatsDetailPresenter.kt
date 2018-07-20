package es.voghdev.playbattlegrounds.features.season.ui.presenter

import es.voghdev.playbattlegrounds.common.Ok
import es.voghdev.playbattlegrounds.common.Presenter
import es.voghdev.playbattlegrounds.common.reslocator.ResLocator
import es.voghdev.playbattlegrounds.features.players.PlayerRepository
import es.voghdev.playbattlegrounds.features.players.model.Player
import es.voghdev.playbattlegrounds.features.season.Season
import es.voghdev.playbattlegrounds.features.season.model.PlayerSeasonInfo
import es.voghdev.playbattlegrounds.format
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

class SeasonStatsDetailPresenter(val resLocator: ResLocator, val playerRepository: PlayerRepository) :
        Presenter<SeasonStatsDetailPresenter.MVPView, SeasonStatsDetailPresenter.Navigator>() {

    suspend override fun initialize() {
        val task = async(CommonPool) {
            playerRepository.getPlayerSeasonInfo(anyPlayer(), anySeason(), System.currentTimeMillis())
        }
        val seasonStatsResponse = task.await()

        if (seasonStatsResponse is Ok) {
            val stats = seasonStatsResponse.b

            showKillDeathRatios(stats)
            showRatings(stats)
        }
    }

    private fun showKillDeathRatios(stats: PlayerSeasonInfo) {
        view?.showSoloKDR("Solo KDR: ${stats.getKillDeathRatioForGameModeStats(stats.statsSolo).format(2)}")
        view?.showSoloFPPKDR("Solo FPP KDR: ${stats.getKillDeathRatioForGameModeStats(stats.statsSoloFPP).format(2)}")
        view?.showDuoKDR("Duo KDR: ${stats.getKillDeathRatioForGameModeStats(stats.statsDuo).format(2)}")
        view?.showDuoFPPKDR("Duo FPP KDR: ${stats.getKillDeathRatioForGameModeStats(stats.statsDuoFPP).format(2)}")
        view?.showSquadKDR("Squad KDR: ${stats.getKillDeathRatioForGameModeStats(stats.statsSquad).format(2)}")
        view?.showSquadFPPKDR("Squad FPP KDR: ${stats.getKillDeathRatioForGameModeStats(stats.statsSquadFPP).format(2)}")
    }

    private fun showRatings(stats: PlayerSeasonInfo) {
        view?.showSoloRating("Solo Rating: ${stats.getRatingForGameModeStats(stats.statsSolo)}")
        view?.showSoloFPPRating("Solo FPP Rating: ${stats.getRatingForGameModeStats(stats.statsSoloFPP)}")
        view?.showDuoRating("Duo Rating: ${stats.getRatingForGameModeStats(stats.statsDuo)}")
        view?.showDuoFPPRating("Duo FPP Rating: ${stats.getRatingForGameModeStats(stats.statsDuoFPP)}")
        view?.showSquadRating("Squad Rating: ${stats.getRatingForGameModeStats(stats.statsSquad)}")
        view?.showSquadFPPRating("Squad FPP Rating: ${stats.getRatingForGameModeStats(stats.statsSquadFPP)}")
    }

    private fun anyPlayer() = Player()

    private fun anySeason() = Season("", true, false)

    interface MVPView {
        fun showSoloKDR(text: String)
        fun showSoloFPPKDR(text: String)
        fun showDuoKDR(text: String)
        fun showDuoFPPKDR(text: String)
        fun showSquadKDR(text: String)
        fun showSquadFPPKDR(text: String)

        fun showSoloRating(text: String)
        fun showSoloFPPRating(text: String)
        fun showDuoRating(text: String)
        fun showDuoFPPRating(text: String)
        fun showSquadRating(text: String)
        fun showSquadFPPRating(text: String)
    }

    interface Navigator
}
