package es.voghdev.playbattlegrounds.features.season.ui.presenter

import android.os.Build
import android.text.format.DateFormat
import es.voghdev.playbattlegrounds.R
import es.voghdev.playbattlegrounds.common.Presenter
import es.voghdev.playbattlegrounds.common.Success
import es.voghdev.playbattlegrounds.features.players.PlayerRepository
import es.voghdev.playbattlegrounds.features.players.model.Player
import es.voghdev.playbattlegrounds.features.season.Season
import es.voghdev.playbattlegrounds.features.season.model.PlayerSeasonInfo
import es.voghdev.playbattlegrounds.features.share.GetImagesPath
import es.voghdev.playbattlegrounds.format
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File

class SeasonStatsDetailPresenter(
    dispatcher: CoroutineDispatcher,
    private val playerRepository: PlayerRepository,
    private val getImagesPath: GetImagesPath
) : Presenter<SeasonStatsDetailPresenter.MVPView, SeasonStatsDetailPresenter.Navigator>(dispatcher) {

    lateinit var initialData: InitialData

    override suspend fun initialize() {
        view?.configureToolbar()
        view?.hideShareButton()
    }

    suspend fun onInitialData(data: InitialData) {
        initialData = data

        val seasonStatsResponse = withContext(dispatcher) {
            playerRepository.getPlayerSeasonInfo(
                Player(data.getPlayerId()),
                Season(
                    id = data.getSeason(),
                    isCurrentSeason = true,
                    isOffSeason = false
                ),
                System.currentTimeMillis()
            )
        }

        if (seasonStatsResponse is Success) {
            val stats = seasonStatsResponse.b

            view?.render(ViewState.Success(
                data.getPlayerName(),
                "Solo KDR: ${stats.statsSolo.kdr().format(2)}",
                getKDRColor(stats.statsSolo.kdr()),
                "${stats.statsSolo.kills} kills, ${stats.statsSolo.losses} deaths",
                "Solo FPP KDR: ${stats.statsSoloFPP.kdr().format(2)}",
                getKDRColor(stats.statsSoloFPP.kdr()),
                "${stats.statsSoloFPP.kills} kills, ${stats.statsSoloFPP.losses} deaths",
                "Duo KDR: ${stats.statsDuo.kdr().format(2)}",
                getKDRColor(stats.statsDuo.kdr()),
                "${stats.statsDuo.kills} kills, ${stats.statsDuo.losses} deaths",
                "Duo FPP KDR: ${stats.statsDuoFPP.kdr().format(2)}",
                getKDRColor(stats.statsDuoFPP.kdr()),
                "${stats.statsDuoFPP.kills} kills, ${stats.statsDuoFPP.losses} deaths",
                "Squad KDR: ${stats.statsSquad.kdr().format(2)}", getKDRColor(stats.statsSquad.kdr()),
                "${stats.statsSquad.kills} kills, ${stats.statsSquad.losses} deaths",
                "Squad FPP KDR: ${stats.statsSquadFPP.kdr().format(2)}", getKDRColor(stats.statsSquadFPP.kdr()),
                "${stats.statsSquadFPP.kills} kills, ${stats.statsSquadFPP.losses} deaths"
            ))

            view?.showShareButton()
            view?.showToolbarTitle(data.getPlayerName())
        }
    }

    fun onShareSeasonStatsButtonClicked(ms: Long) {
        val now = DateFormat.format("yyyyMMdd_hhmmss", ms)
        val pathResult = getImagesPath.getImagesPath()
        if (pathResult is Success) {
            val imageFile = File(pathResult.b, "$now.png")
            view?.takeScreenshot(imageFile.absolutePath)
            if (initialData.getSdkVersion() >= Build.VERSION_CODES.N) {
                view?.sharePlayerStatsNougat(imageFile.absolutePath)
            } else {
                view?.sharePlayerStatsPreNougat(imageFile.absolutePath)
            }
        }
    }

    fun getKDRColor(kdr: Float): Int =
        when {
            kdr > 2f -> R.color.blue
            kdr > 1f -> R.color.green
            kdr > 0.75f -> R.color.colorPrimary
            else -> R.color.light_red
        }

    sealed class ViewState {
        object Failure : ViewState()
        object Loading : ViewState()
        data class Success(
            val toolbarTitle: String,
            val soloKDR: String,
            val soloColor: Int,
            val soloSummary: String,
            val soloFppKDR: String,
            val soloFppColor: Int,
            val soloFppSummary: String,
            val duoKDR: String,
            val duoColor: Int,
            val duoSummary: String,
            val duoFppKDR: String,
            val duoFppColor: Int,
            val duoFppSummary: String,
            val squadKDR: String,
            val squadColor: Int,
            val squadSummary: String,
            val squadFppKDR: String,
            val squadFppColor: Int,
            val squadFppSummary: String
        ) : ViewState()
    }

    interface InitialData {
        fun getPlayerId(): String
        fun getPlayerName(): String
        fun getSeason(): String
        fun getSdkVersion(): Int
    }

    interface MVPView {
        fun render(state: ViewState)

        fun configureToolbar()
        fun showToolbarTitle(title: String)

        fun takeScreenshot(path: String)
        fun sharePlayerStatsNougat(path: String)
        fun sharePlayerStatsPreNougat(path: String)
        fun showShareButton()
        fun hideShareButton()
    }

    interface Navigator
}
