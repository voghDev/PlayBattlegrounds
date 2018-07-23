package es.voghdev.playbattlegrounds.features.season.ui.presenter

import android.content.Intent
import es.voghdev.playbattlegrounds.R
import es.voghdev.playbattlegrounds.common.EXTRA_PLAYER_ID
import es.voghdev.playbattlegrounds.common.EXTRA_SEASON
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
        view?.configureToolbar()
    }

    suspend fun onInitialData(data: InitialData) {
        val task = async(CommonPool) {
            playerRepository.getPlayerSeasonInfo(Player(data.getPlayerId()), Season(data.getSeason(), true, false), System.currentTimeMillis())
        }
        val seasonStatsResponse = task.await()

        if (seasonStatsResponse is Ok) {
            val stats = seasonStatsResponse.b

            showKillDeathRatios(stats)
            showSummaries(stats)
            showRatings(stats)
        }
    }

    private fun showKillDeathRatios(stats: PlayerSeasonInfo) {
        view?.showSoloKDR("Solo KDR: ${stats.statsSolo.kdr().format(2)}", getKDRColor(stats.statsSolo.kdr()))
        view?.showSoloFPPKDR("Solo FPP KDR: ${stats.statsSoloFPP.kdr().format(2)}", getKDRColor(stats.statsSoloFPP.kdr()))
        view?.showDuoKDR("Duo KDR: ${stats.statsDuo.kdr().format(2)}", getKDRColor(stats.statsDuo.kdr()))
        view?.showDuoFPPKDR("Duo FPP KDR: ${stats.statsDuoFPP.kdr().format(2)}", getKDRColor(stats.statsDuoFPP.kdr()))
        view?.showSquadKDR("Squad KDR: ${stats.statsSquad.kdr().format(2)}", getKDRColor(stats.statsSquad.kdr()))
        view?.showSquadFPPKDR("Squad FPP KDR: ${stats.statsSquadFPP.kdr().format(2)}", getKDRColor(stats.statsSquadFPP.kdr()))
    }

    private fun showSummaries(stats: PlayerSeasonInfo) {
        view?.showSoloSummary("${stats.statsSolo.kills} kills, ${stats.statsSolo.losses} deaths")
        view?.showSoloFPPSummary("${stats.statsSoloFPP.kills} kills, ${stats.statsSoloFPP.losses} deaths")
        view?.showDuoSummary("${stats.statsDuo.kills} kills, ${stats.statsDuo.losses} deaths")
        view?.showDuoFPPSummary("${stats.statsDuoFPP.kills} kills, ${stats.statsDuoFPP.losses} deaths")
        view?.showSquadSummary("${stats.statsSquad.kills} kills, ${stats.statsSquad.losses} deaths")
        view?.showSquadFPPSummary("${stats.statsSquadFPP.kills} kills, ${stats.statsSquadFPP.losses} deaths")
    }

    private fun showRatings(stats: PlayerSeasonInfo) {
        view?.showSoloRating("Solo Rating: ${stats.statsSolo.rating()}", getRatingColor(stats.statsSolo.rating()))
        view?.showSoloFPPRating("Solo FPP Rating: ${stats.statsSoloFPP.rating()}", getRatingColor(stats.statsSoloFPP.rating()))
        view?.showDuoRating("Duo Rating: ${stats.statsDuo.rating()}", getRatingColor(stats.statsDuo.rating()))
        view?.showDuoFPPRating("Duo FPP Rating: ${stats.statsDuoFPP.rating()}", getRatingColor(stats.statsDuoFPP.rating()))
        view?.showSquadRating("Squad Rating: ${stats.statsSquad.rating()}", getRatingColor(stats.statsSquad.rating()))
        view?.showSquadFPPRating("Squad FPP Rating: ${stats.statsSquadFPP.rating()}", getRatingColor(stats.statsSquadFPP.rating()))
    }

    fun getRatingColor(rating: Int): Int =
        when {
            rating > 2200 -> R.color.blue
            rating > 1900 -> R.color.green
            rating > 1700 -> R.color.colorPrimary
            else -> R.color.light_red
        }

    fun getKDRColor(kdr: Float): Int =
        when {
            kdr > 2f -> R.color.blue
            kdr > 1f -> R.color.green
            kdr > 0.75f -> R.color.colorPrimary
            else -> R.color.light_red
        }

    private fun anyPlayer() = Player()

    private fun anySeason() = Season("", true, false)

    interface InitialData {
        fun getPlayerId(): String
        fun getSeason(): String
    }

    class AndroidInitialData(val intent: Intent?) : InitialData {
        override fun getPlayerId(): String =
            intent?.getStringExtra(EXTRA_PLAYER_ID) ?: ""

        override fun getSeason(): String =
            intent?.getStringExtra(EXTRA_SEASON) ?: ""
    }

    interface MVPView {
        fun configureToolbar()

        fun showSoloKDR(text: String, statColorResId: Int = R.color.colorPrimary)
        fun showSoloFPPKDR(text: String, statColorResId: Int = R.color.colorPrimary)
        fun showDuoKDR(text: String, statColorResId: Int = R.color.colorPrimary)
        fun showDuoFPPKDR(text: String, statColorResId: Int = R.color.colorPrimary)
        fun showSquadKDR(text: String, statColorResId: Int = R.color.colorPrimary)
        fun showSquadFPPKDR(text: String, statColorResId: Int = R.color.colorPrimary)

        fun showSoloSummary(text: String)
        fun showSoloFPPSummary(text: String)
        fun showDuoSummary(text: String)
        fun showDuoFPPSummary(text: String)
        fun showSquadSummary(text: String)
        fun showSquadFPPSummary(text: String)

        fun showSoloRating(text: String, statColorResId: Int = R.color.colorPrimary)
        fun showSoloFPPRating(text: String, statColorResId: Int = R.color.colorPrimary)
        fun showDuoRating(text: String, statColorResId: Int = R.color.colorPrimary)
        fun showDuoFPPRating(text: String, statColorResId: Int = R.color.colorPrimary)
        fun showSquadRating(text: String, statColorResId: Int = R.color.colorPrimary)
        fun showSquadFPPRating(text: String, statColorResId: Int = R.color.colorPrimary)
    }

    interface Navigator
}
