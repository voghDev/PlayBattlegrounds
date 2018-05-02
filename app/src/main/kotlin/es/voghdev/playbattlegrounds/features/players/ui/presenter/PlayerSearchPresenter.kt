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
import es.voghdev.playbattlegrounds.features.matches.usecase.GetMatchById
import es.voghdev.playbattlegrounds.features.players.model.Player
import es.voghdev.playbattlegrounds.features.players.usecase.GetPlayerByName
import es.voghdev.playbattlegrounds.toDate
import org.jetbrains.anko.doAsync

class PlayerSearchPresenter(val resLocator: ResLocator, val getPlayerByName: GetPlayerByName, val getMatchById: GetMatchById) :
        Presenter<PlayerSearchPresenter.MVPView, PlayerSearchPresenter.Navigator>() {

    override suspend fun initialize() {

    }

    fun onRootViewClicked() {
        view?.hideSoftKeyboard()
    }

    fun onSendButtonClicked(playerName: String) = doAsync {
        val result = getPlayerByName.getPlayerByName(playerName.toLowerCase())
        when (result) {
            is Ok -> {
                view?.showPlayerName(result.b.name)
                view?.hideSoftKeyboard()

                requestPlayerMatches(result.b)
            }
            is Fail -> {
                view?.showError(result.a.message)
            }
        }
    }

    private fun requestPlayerMatches(player: Player) {
        if (player.matches.isNotEmpty()) {
            val result = getMatchById.getMatchById(player.matches.first().id)
            when (result) {
                is Ok ->
                    view?.showLastMatchInfo("Last match: ${result.b.date.toDate()}")
                is Fail -> {
                    view?.showError(result.a.message)
                }
            }

            // player.matches.forEach {  }
        }
    }

    interface MVPView {
        fun showPlayerName(name: String)
        fun showLastMatchInfo(text: String)
        fun showError(message: String)
        fun hideSoftKeyboard()
    }

    interface Navigator {

    }
}
