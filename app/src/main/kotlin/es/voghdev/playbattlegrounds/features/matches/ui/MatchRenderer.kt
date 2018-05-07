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
package es.voghdev.playbattlegrounds.features.matches.ui

import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.pedrogomez.renderers.Renderer
import es.voghdev.playbattlegrounds.R
import es.voghdev.playbattlegrounds.colorStateList
import es.voghdev.playbattlegrounds.features.matches.Match
import es.voghdev.playbattlegrounds.toDate

class MatchRenderer(val listener: OnRowClicked?) : Renderer<Match>() {
    var tv_title: TextView? = null
    var tv_rank: TextView? = null
    var tv_kills: TextView? = null
    var tv_game_mode: TextView? = null
    var iv_map: ImageView? = null

    override fun hookListeners(rootView: View?) {
        rootView?.setOnClickListener {
            listener?.onMatchClicked(content)
        }
    }

    override fun setUpView(rootView: View?) {
        tv_title = rootView?.findViewById(R.id.tv_title)
        tv_rank = rootView?.findViewById(R.id.tv_rank)
        tv_kills = rootView?.findViewById(R.id.tv_kills)
        tv_game_mode = rootView?.findViewById(R.id.tv_game_mode)
        iv_map = rootView?.findViewById(R.id.iv_map)
    }

    override fun inflate(inflater: LayoutInflater?, parent: ViewGroup?): View {
        return inflater?.inflate(R.layout.row_match, parent, false) ?: View(context);
    }

    override fun render() {
        renderMatchDate(content)
        renderGameMode(content)
        renderNumberOfKills(content)
        renderWinPlace(content)
        renderMap(content)
    }

    private fun renderMatchDate(content: Match) {
        tv_title?.text = content.date.toDate()
    }

    private fun renderGameMode(content: Match) {
        tv_game_mode?.text = content.gameMode

        tv_game_mode?.backgroundTintList = context.colorStateList(when (content.gameMode) {
            "solo-fpp" -> R.color.green
            "solo" -> R.color.dark_green
            "squad-fpp" -> R.color.red
            "squad" -> R.color.light_red
            "duo-fpp" -> R.color.blue
            "duo" -> R.color.dark_blue
            else -> R.color.colorPrimary
        })
    }

    private fun renderNumberOfKills(content: Match?) {
        tv_kills?.text = context.resources.getQuantityString(R.plurals.kills,
                content?.numberOfKillsForCurrentPlayer ?: 0,
                content?.numberOfKillsForCurrentPlayer ?: 0)

        tv_kills?.setTextColor(context.colorStateList(when (content?.numberOfKillsForCurrentPlayer) {
            0, 1 -> R.color.red
            2 -> R.color.colorPrimary
            3, 4, 5 -> R.color.green
            else -> R.color.blue
        }))
    }

    private fun renderWinPlace(content: Match?) {
        tv_rank?.text = "#${content?.placeForCurrentPlayer}"
    }

    private fun renderMap(content: Match) {
        iv_map?.setImageResource(
                when {
                    content.map.toLowerCase().contains("erangel") -> R.mipmap.erangel_xsmall
                    content.map.toLowerCase().contentEquals("desert") -> R.mipmap.miramar_xsmall
                    else -> R.mipmap.miramar_xsmall
                })
    }

    interface OnRowClicked {
        fun onMatchClicked(match: Match)
    }
}
