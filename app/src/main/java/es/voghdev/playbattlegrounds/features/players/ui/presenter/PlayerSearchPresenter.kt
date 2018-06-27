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

import com.appandweb.weevento.ui.presenter.Presenter
import es.voghdev.playbattlegrounds.R
import es.voghdev.playbattlegrounds.common.Fail
import es.voghdev.playbattlegrounds.common.Ok
import es.voghdev.playbattlegrounds.common.reslocator.ResLocator
import es.voghdev.playbattlegrounds.features.matches.Match
import es.voghdev.playbattlegrounds.features.matches.MatchRepository
import es.voghdev.playbattlegrounds.features.onboarding.usecase.GetPlayerAccount
import es.voghdev.playbattlegrounds.features.players.PlayerRepository
import es.voghdev.playbattlegrounds.features.players.model.Player
import es.voghdev.playbattlegrounds.features.players.usecase.IsContentAvailableForPlayer
import es.voghdev.playbattlegrounds.features.season.model.PlayerSeasonInfo
import es.voghdev.playbattlegrounds.features.season.usecase.GetCurrentSeason
import es.voghdev.playbattlegrounds.features.season.usecase.GetPlayerSeasonInfo
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

class PlayerSearchPresenter(val resLocator: ResLocator,
                            val playerRepository: PlayerRepository,
                            val matchRepository: MatchRepository,
                            val getPlayerAccount: GetPlayerAccount,
                            val getCurrentSeason: GetCurrentSeason,
                            val getPlayerSeasonInfo: GetPlayerSeasonInfo,
                            val isContentAvailableForPlayer: IsContentAvailableForPlayer) :
        Presenter<PlayerSearchPresenter.MVPView, PlayerSearchPresenter.Navigator>() {

    val RED = "#ff9900"
    var player = Player()
    var matchesFrom = 0

    suspend override fun initialize() {
        val account = getPlayerAccount.getPlayerAccount()
        if (account is Ok && account.b.isNotEmpty())
            view?.fillPlayerAccount(account.b)
    }

    suspend fun onInitialData(data: InitialData) {
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
        view?.hideContentAvailableButton()

        val task = async(CommonPool) {
            playerRepository.getPlayerByName(playerName)
        }

        val result = task.await()

        when (result) {
            is Ok -> {
                player = result.b
                view?.showPlayerFoundMessage("Found: ${result.b.name}. Loading matches...")
                view?.hideSoftKeyboard()

                view?.clearList()

                requestPlayerSeasonStats(result.b)

                requestPlayerMatches(result.b)

                if (player.matches.size > matchesFrom + 5)
                    view?.addLoadMoreItem()
            }
            is Fail -> {
                view?.showDialog(resLocator.getString(R.string.error), result.a.message)
                view?.hideLoading()
            }
        }
    }

    private suspend fun requestPlayerMatches(player: Player, from: Int = 0, n: Int = 5) {
        if (player.matches.isNotEmpty()) {

            var errors = 0
            player.matches.subList(from, player.matches.size).take(n).forEach {
                val task = async(CommonPool) {
                    matchRepository.getMatchById(it.id)
                }

                val result = task.await()

                when (result) {
                    is Ok -> {
                        val name = player.name
                        val kills = maxOf(result.b.getNumberOfKills(name), result.b.numberOfKillsForCurrentPlayer)
                        val place = maxOf(result.b.getWinPlaceForParticipant(name), result.b.placeForCurrentPlayer)
                        val copy = result.b.copy(
                                numberOfKillsForCurrentPlayer = kills,
                                placeForCurrentPlayer = place)

                        matchRepository.insertMatch(copy)

                        view?.addMatch(copy)
                    }
                    is Fail ->
                        ++errors
                }
            }

            view?.hideLoading()

            val contentResult = isContentAvailableForPlayer.isContentAvailableForPlayer(player)
            if (contentResult is Ok && contentResult.b)
                view?.showContentAvailableButton()

            if (errors > 0)
                view?.showError("Could not load $errors matches")
        }
    }

    private suspend fun requestPlayerSeasonStats(player: Player) {
        val currentSeasonResult = async(CommonPool) {
            getCurrentSeason.getCurrentSeason()
        }.await()

        if (currentSeasonResult is Ok) {
            val seasonInfoTask = async(CommonPool) {
                getPlayerSeasonInfo.getPlayerSeasonInfo(player, currentSeasonResult.b)
            }

            val seasonInfo = seasonInfoTask.await()

            if (seasonInfo is Ok) {
                view?.addPlayerStatsRow(seasonInfo.b)
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
        /* Should navigate to a screen with all your KDRs and Ratings */
    }

    fun onContentButtonClicked() {

    }

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
    }

    interface Navigator

    interface InitialData {
        fun getPlayerName(): String
    }
}
