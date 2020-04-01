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
package es.voghdev.playbattlegrounds.features.season

import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pedrogomez.renderers.Renderer
import es.voghdev.playbattlegrounds.R
import es.voghdev.playbattlegrounds.features.season.model.PlayerSeasonInfo
import es.voghdev.playbattlegrounds.format

class PlayerSeasonInfoRenderer(val listener: OnRowClicked?) : Renderer<PlayerSeasonInfo>() {
    var tv_kdr: TextView? = null

    override fun hookListeners(rootView: View?) {
        rootView?.setOnClickListener {
            listener?.onPlayerSeasonInfoClicked(content)
        }
    }

    override fun setUpView(rootView: View?) {
        tv_kdr = rootView?.findViewById(R.id.tv_kdr)
    }

    override fun inflate(inflater: LayoutInflater?, parent: ViewGroup?): View {
        return inflater?.inflate(R.layout.row_player_season_info, parent, false) ?: View(context)
    }

    override fun render() {
        renderPlayerKillDeathRatio()
    }

    private fun renderPlayerKillDeathRatio() {
        val emptyText = context.getString(R.string.no_matches_found)
        val bestKdrStats = content.getBestKDRStats()
        val kdr = content.getKillDeathRatioForGameModeStats(bestKdrStats)

        tv_kdr?.text = "${kdr.format(2)}"

        tv_kdr?.setTextColor(
            when {
                kdr > 2f -> ContextCompat.getColor(context, R.color.blue)
                kdr > 1f -> ContextCompat.getColor(context, R.color.green)
                kdr > 0.75f -> ContextCompat.getColor(context, R.color.colorPrimary)
                else -> ContextCompat.getColor(context, R.color.light_red)
            }
        )
    }

    interface OnRowClicked {
        fun onPlayerSeasonInfoClicked(playerSeasonInfo: PlayerSeasonInfo)
    }
}
