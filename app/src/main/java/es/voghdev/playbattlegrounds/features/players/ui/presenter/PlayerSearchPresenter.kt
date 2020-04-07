/*
 * Copyright (C) 2018 Olmo Gallegos Hernández.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.voghdev.playbattlegrounds.features.players.ui.presenter

import android.os.Build
import android.text.format.DateFormat
import arrow.core.Either
import es.voghdev.playbattlegrounds.R
import es.voghdev.playbattlegrounds.common.Failure
import es.voghdev.playbattlegrounds.common.Presenter
import es.voghdev.playbattlegrounds.common.Success
import es.voghdev.playbattlegrounds.common.reslocator.ResLocator
import es.voghdev.playbattlegrounds.features.matches.Match
import es.voghdev.playbattlegrounds.features.matches.MatchRepository
import es.voghdev.playbattlegrounds.features.onboarding.usecase.GetPlayerAccount
import es.voghdev.playbattlegrounds.features.onboarding.usecase.GetPlayerRegion
import es.voghdev.playbattlegrounds.features.players.PlayerRepository
import es.voghdev.playbattlegrounds.features.players.model.Content
import es.voghdev.playbattlegrounds.features.players.model.Player
import es.voghdev.playbattlegrounds.features.players.usecase.IsContentAvailableForPlayer
import es.voghdev.playbattlegrounds.features.season.Season
import es.voghdev.playbattlegrounds.features.season.model.PlayerSeasonGameModeStats
import es.voghdev.playbattlegrounds.features.season.model.PlayerSeasonInfo
import es.voghdev.playbattlegrounds.features.season.usecase.GetCurrentSeason
import es.voghdev.playbattlegrounds.features.share.GetImagesPath
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File

class PlayerSearchPresenter(
    dispatcher: CoroutineDispatcher,
    private val resLocator: ResLocator,
    private val playerRepository: PlayerRepository,
    private val matchRepository: MatchRepository,
    private val getPlayerAccount: GetPlayerAccount,
    private val getCurrentSeason: GetCurrentSeason,
    private val isContentAvailableForPlayer: IsContentAvailableForPlayer,
    private val getPlayerRegion: GetPlayerRegion,
    private val getImagesPath: GetImagesPath,
    private var sdkVersion: Int
) :
    Presenter<PlayerSearchPresenter.MVPView, PlayerSearchPresenter.Navigator>(dispatcher) {

    private val DEFAULT_REGION = "pc-eu"
    private var player = Player()
    private var seasonInfo = createEmptyPlayerSeasonInfo()
    private var matchesFrom = 0
    private var enableAdditionalContents = true
    private var region = DEFAULT_REGION

    override suspend fun initialize() {
        val account = getPlayerAccount.getPlayerAccount()
        if (account is Success && account.b.isNotEmpty())
            view?.fillPlayerAccount(account.b)
    }

    suspend fun onInitialData(data: InitialData) {
        enableAdditionalContents = data.additionalContentsEnabled()

        region = if (data.getRegion().isNotEmpty()) {
            data.getRegion()
        } else {
            (getPlayerRegion.getPlayerRegion() as? Success)?.b?.name ?: DEFAULT_REGION
        }

        if (data.getPlayerName().isNotEmpty()) {
            view?.fillPlayerAccount(data.getPlayerName())

            requestPlayerData(data.getPlayerName())
        }
    }

    fun onRootViewClicked() {
        view?.hideSoftKeyboard()
    }

    suspend fun onSendButtonClicked(playerName: String) {
        requestPlayerData(playerName)
    }

    private suspend fun requestPlayerData(playerName: String) {
        view?.showLoading()
        view?.hideShareButton()
        view?.hideContentAvailableButton()

        val response = withContext(dispatcher) {
            playerRepository.getPlayerByName(playerName, region)
        }

        when (response) {
            is Success -> {
                player = response.b
                view?.showPlayerFoundMessage(resLocator.getString(R.string.player_found_param, player.name))
                view?.hideSoftKeyboard()

                view?.clearList()

                requestPlayerSeasonStats(response.b)

                requestPlayerMatches(response.b)

                if (player.matches.size > matchesFrom + 5)
                    view?.addLoadMoreItem()
            }
            is Failure -> {
                view?.showDialog(resLocator.getString(R.string.error), response.a.message)
                view?.hideLoading()
            }
        }
    }

    private suspend fun requestPlayerMatches(player: Player, from: Int = 0, n: Int = 5) {
        if (player.matches.isNotEmpty()) {
            var errors = 0

            view?.hideEmptyCase()

            player.matches.subList(from, player.matches.size).take(n).forEach {
                val result = withContext(dispatcher) {
                    matchRepository.getMatchById(it.id)
                }

                when (result) {
                    is Success -> {
                        val name = player.name
                        val kills = maxOf(result.b.getNumberOfKills(name), result.b.numberOfKillsForCurrentPlayer)
                        val place = maxOf(result.b.getWinPlaceForParticipant(name), result.b.placeForCurrentPlayer)
                        val copy = result.b.copy(
                            numberOfKillsForCurrentPlayer = kills,
                            placeForCurrentPlayer = place)

                        with(it) {
                            numberOfKillsForCurrentPlayer = kills
                            placeForCurrentPlayer = place
                            date = result.b.date
                            gameMode = result.b.gameMode
                        }

                        matchRepository.insertMatch(copy)

                        view?.addMatch(copy)
                    }
                    is Failure ->
                        ++errors
                }
            }

            val contentResult = isContentAvailableForPlayer.isContentAvailableForPlayer(player)
            if (contentResult is Success && contentResult.b && enableAdditionalContents)
                view?.showContentAvailableButton()

            view?.showShareButton()

            if (errors > 0)
                view?.showError("Could not load $errors matches")
        } else {
            view?.showEmptyCase()
        }

        view?.hideLoading()
    }

    private suspend fun requestPlayerSeasonStats(player: Player) {
        val currentSeasonResult = withContext(dispatcher) {
            getCurrentSeason.getCurrentSeason()
        }

        if (currentSeasonResult is Success) {
            val seasonInfoResult = withContext(dispatcher) {
                playerRepository.getPlayerSeasonInfo(player, currentSeasonResult.b, System.currentTimeMillis())
            }

            if (seasonInfoResult is Success) {
                seasonInfo = seasonInfoResult.b
                view?.addPlayerStatsRow(seasonInfoResult.b)

                if (seasonInfo.isEmpty()) {
                    view?.showNoMatchesInSeasonMessage(
                        resLocator.getString(R.string.no_matches_in_season_param, player.name))
                }
            }
        }
    }

    fun onMatchClicked(match: Match) {
        if (match.isDuoOrSquad()) {
            val teammates = match.participants
                .filter { it.place == match.placeForCurrentPlayer }
                .map { "${it.name} (${it.kills} kills)" }
                .joinToString("\n")

            view?.showDialog(
                "#${match.placeForCurrentPlayer}",
                if (teammates.isNotEmpty()) teammates
                else resLocator.getString(R.string.no_teammates_info))
        }
    }

    suspend fun onLoadMoreMatchesClicked() {
        view?.removeLoadMoreItem()

        matchesFrom += 6

        requestPlayerMatches(player, matchesFrom, 5)

        if (player.matches.size > matchesFrom + 5)
            view?.addLoadMoreItem()
    }

    fun onPlayerSeasonInfoClicked(playerSeasonInfo: PlayerSeasonInfo) {
        val season = (getCurrentSeason.getCurrentSeason() as? Either.Right)?.b
        navigator?.launchPlayerSeasonInfoScreen(player, season)
    }

    fun onContentButtonClicked() {
        val content: Content = getContentForPlayer(player)

        navigator?.launchContentDetailScreen(content)
    }

    fun onShareStatsButtonClicked(ms: Long) {
        val now = DateFormat.format("yyyyMMdd_hhmmss", ms)
        val pathResult = getImagesPath.getImagesPath()
        if (pathResult is Success) {
            val imageFile = File(pathResult.b, "$now.png")
            view?.takeScreenshot(imageFile.absolutePath)
            if (sdkVersion >= Build.VERSION_CODES.N) {
                view?.sharePlayerStatsNougat(imageFile.absolutePath)
            } else {
                view?.sharePlayerStatsPreNougat(imageFile.absolutePath)
            }
        }
    }

    private fun getContentForPlayer(player: Player): Content {
        val bestKDR = seasonInfo.getKillDeathRatioForGameModeStats(seasonInfo.getBestKDRStats())

        if (player.name.equals("ByRubi9"))
            return Content(id = 9L)

        if (player.hasMatchesWithZeroKills(3))
            return Content(id = 0L)

        if (bestKDR > 5f)
            return Content(id = 6L)

        if (player.hasWins())
            return Content(id = 4L)

        if (player.hasTop10MatchesWithLessThan(5, 15))
            return Content(id = 5L)

        if (player.hasMostlyTPPMatches())
            return Content(id = 1L)

        return Content(id = listOf(2L, 3L).shuffled().first())
    }

    fun Player.hasMostlyTPPMatches(): Boolean {
        val tpp = matches.count { it.gameMode in listOf("solo", "squad", "duo") }
        val rest = matches.size - tpp

        return tpp > rest
    }

    fun Player.hasMatchesWithZeroKills(n: Int): Boolean =
        matches.sortedByDescending { it.date }.take(n).sumBy { it.numberOfKillsForCurrentPlayer } == 0

    fun Player.hasWins(): Boolean =
        matches.count { it.placeForCurrentPlayer == 1 } > 0

    fun Player.hasTop10MatchesWithLessThan(n: Int, kills: Int): Boolean {
        val lastMatches = matches.sortedByDescending { it.date }.take(10)
        return lastMatches.count { it.placeForCurrentPlayer <= 10 } > n &&
            lastMatches.sumBy { it.numberOfKillsForCurrentPlayer } < kills
    }

    private fun createEmptyPlayerSeasonInfo() = PlayerSeasonInfo(
        PlayerSeasonGameModeStats(),
        PlayerSeasonGameModeStats(),
        PlayerSeasonGameModeStats(),
        PlayerSeasonGameModeStats(),
        PlayerSeasonGameModeStats(),
        PlayerSeasonGameModeStats()
    )

    interface MVPView {
        fun showPlayerFoundMessage(message: String)
        fun showError(message: String)
        fun showDialog(title: String, message: String)
        fun clearList()
        fun addMatch(match: Match)
        fun hideSoftKeyboard()
        fun showLoading()
        fun hideLoading()
        fun fillPlayerAccount(account: String)
        fun addPlayerStatsRow(seasonInfo: PlayerSeasonInfo)
        fun addLoadMoreItem()
        fun removeLoadMoreItem()
        fun hideContentAvailableButton()
        fun showContentAvailableButton()
        fun showEmptyCase()
        fun hideEmptyCase()
        fun showNoMatchesInSeasonMessage(message: String)
        fun sharePlayerStatsNougat(path: String)
        fun sharePlayerStatsPreNougat(path: String)
        fun takeScreenshot(path: String)
        fun showShareButton()
        fun hideShareButton()
    }

    interface Navigator {
        fun launchContentDetailScreen(content: Content)
        fun launchPlayerSeasonInfoScreen(player: Player, season: Season?)
    }

    interface InitialData {
        fun getPlayerName(): String
        fun additionalContentsEnabled(): Boolean
        fun getRegion(): String
    }
}
