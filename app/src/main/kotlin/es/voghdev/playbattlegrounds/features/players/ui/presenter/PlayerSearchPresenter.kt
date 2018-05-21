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
import es.voghdev.playbattlegrounds.common.Fail
import es.voghdev.playbattlegrounds.common.Ok
import es.voghdev.playbattlegrounds.common.reslocator.ResLocator
import es.voghdev.playbattlegrounds.features.matches.Match
import es.voghdev.playbattlegrounds.features.matches.MatchRepository
import es.voghdev.playbattlegrounds.features.onboarding.usecase.GetPlayerAccount
import es.voghdev.playbattlegrounds.features.players.model.Player
import es.voghdev.playbattlegrounds.features.players.usecase.GetPlayerByName
import es.voghdev.playbattlegrounds.features.season.model.PlayerSeasonInfo
import es.voghdev.playbattlegrounds.features.season.usecase.GetCurrentSeason
import es.voghdev.playbattlegrounds.features.season.usecase.GetPlayerSeasonInfo
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

class PlayerSearchPresenter(val resLocator: ResLocator,
                            val getPlayerByName: GetPlayerByName,
                            val matchRepository: MatchRepository,
                            val getPlayerAccount: GetPlayerAccount,
                            val getCurrentSeason: GetCurrentSeason,
                            val getPlayerSeasonInfo: GetPlayerSeasonInfo) :
        Presenter<PlayerSearchPresenter.MVPView, PlayerSearchPresenter.Navigator>() {

    val RED = "#ff9900"

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

        val task = async(CommonPool) {
            getPlayerByName.getPlayerByName(playerName)
        }

        val result = task.await()

        when (result) {
            is Ok -> {
                view?.showPlayerFoundMessage("Found: ${result.b.name}. Loading matches...")
                view?.hideSoftKeyboard()

                view?.clearList()

                requestPlayerSeasonStats(result.b)

                requestPlayerMatches(result.b)
            }
            is Fail -> {
                view?.showError(result.a.message)
                view?.hideLoading()
            }
        }
    }

    private suspend fun requestPlayerMatches(player: Player) {
        if (player.matches.isNotEmpty()) {

            var errors = 0
            player.matches.subList(0, player.matches.size).take(5).forEach {
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
        /* Should navigate to a screen with Match details? */
    }

    fun onPlayerSeasonInfoClicked(playerSeasonInfo: PlayerSeasonInfo) {
        /* Should navigate to a screen with all your KDRs and Ratings */
    }

    interface MVPView {
        fun showPlayerFoundMessage(message: String)
        fun showError(message: String)
        fun clearList()
        fun addMatch(match: Match)
        fun hideSoftKeyboard()
        fun showLoading()
        fun hideLoading()
        fun fillPlayerAccount(account: String)
        fun addPlayerStatsRow(seasonInfo: PlayerSeasonInfo)
    }

    interface Navigator

    interface InitialData {
        fun getPlayerName(): String
    }
}
