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
package es.voghdev.playbattlegrounds.features.players.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.pedrogomez.renderers.RVRendererAdapter
import com.pedrogomez.renderers.RendererBuilder
import es.voghdev.playbattlegrounds.R
import es.voghdev.playbattlegrounds.common.BaseActivity
import es.voghdev.playbattlegrounds.common.EXTRA_CONTENT_ID
import es.voghdev.playbattlegrounds.common.EXTRA_PLAYER_ID
import es.voghdev.playbattlegrounds.common.EXTRA_PLAYER_NAME
import es.voghdev.playbattlegrounds.common.EXTRA_SEASON
import es.voghdev.playbattlegrounds.common.asApp
import es.voghdev.playbattlegrounds.common.reslocator.ResLocator
import es.voghdev.playbattlegrounds.common.ui.ColoredSnackbar
import es.voghdev.playbattlegrounds.common.ui.ListEntity
import es.voghdev.playbattlegrounds.features.matches.Match
import es.voghdev.playbattlegrounds.features.matches.MatchRepository
import es.voghdev.playbattlegrounds.features.matches.ui.LoadMore
import es.voghdev.playbattlegrounds.features.matches.ui.LoadMoreRenderer
import es.voghdev.playbattlegrounds.features.matches.ui.MatchRenderer
import es.voghdev.playbattlegrounds.features.onboarding.usecase.GetPlayerAccount
import es.voghdev.playbattlegrounds.features.onboarding.usecase.GetPlayerRegion
import es.voghdev.playbattlegrounds.features.players.PlayerRepository
import es.voghdev.playbattlegrounds.features.players.model.Content
import es.voghdev.playbattlegrounds.features.players.model.Player
import es.voghdev.playbattlegrounds.features.players.ui.presenter.PlayerSearchInitialData
import es.voghdev.playbattlegrounds.features.players.ui.presenter.PlayerSearchPresenter
import es.voghdev.playbattlegrounds.features.players.usecase.IsContentAvailableForPlayer
import es.voghdev.playbattlegrounds.features.season.PlayerSeasonInfoRenderer
import es.voghdev.playbattlegrounds.features.season.Season
import es.voghdev.playbattlegrounds.features.season.model.PlayerSeasonInfo
import es.voghdev.playbattlegrounds.features.season.ui.activity.SeasonStatsDetailActivity
import es.voghdev.playbattlegrounds.features.season.usecase.GetCurrentSeason
import es.voghdev.playbattlegrounds.features.share.GetImagesPath
import es.voghdev.playbattlegrounds.hideSoftKeyboard
import es.voghdev.playbattlegrounds.shareFileNougat
import es.voghdev.playbattlegrounds.shareFilePreNougat
import es.voghdev.playbattlegrounds.startActivity
import es.voghdev.playbattlegrounds.takeAScreenshot
import es.voghdev.playbattlegrounds.ui
import kotlinx.android.synthetic.main.activity_player_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class PlayerSearchActivity : BaseActivity(), KodeinAware, PlayerSearchPresenter.MVPView, PlayerSearchPresenter.Navigator, MatchRenderer.OnRowClicked, PlayerSeasonInfoRenderer.OnRowClicked, LoadMoreRenderer.OnRowClicked {
    override val kodein: Kodein by lazy { applicationContext.asApp().kodein }

    val playerRepository: PlayerRepository by instance()
    val matchRepository: MatchRepository by instance()
    val getPlayerAccount: GetPlayerAccount by instance()
    val getCurrentSeason: GetCurrentSeason by instance()
    val getPlayerRegion: GetPlayerRegion by instance()
    val isContentAvailable: IsContentAvailableForPlayer by instance()
    val getImagesPath: GetImagesPath by instance()
    val resLocator: ResLocator by instance()
    var adapter: RVRendererAdapter<ListEntity>? = null
    var contentAvailableItem: MenuItem? = null
    var shareItem: MenuItem? = null

    var presenter: PlayerSearchPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val matchRenderer = MatchRenderer(this)
        val seasonRenderer = PlayerSeasonInfoRenderer(this)
        val loadMoreRenderer = LoadMoreRenderer(this)
        val rendererBuilder = RendererBuilder<ListEntity>()
            .bind(Match::class.java, matchRenderer)
            .bind(PlayerSeasonInfo::class.java, seasonRenderer)
            .bind(LoadMore::class.java, loadMoreRenderer)
        adapter = RVRendererAdapter<ListEntity>(rendererBuilder)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        presenter = PlayerSearchPresenter(
            Dispatchers.IO,
            resLocator,
            playerRepository,
            matchRepository,
            getPlayerAccount,
            getCurrentSeason,
            isContentAvailable,
            getPlayerRegion,
            getImagesPath,
            Build.VERSION.SDK_INT
        )
        presenter?.view = this
        presenter?.navigator = this

        coroutineScope.launch {
            presenter?.initialize()

            presenter?.onInitialData(PlayerSearchInitialData(intent))
        }

        btn_send.setOnClickListener {
            coroutineScope.launch {
                presenter?.onSendButtonClicked(et_username.text.toString().trim())
            }
        }

        rootView.setOnClickListener {
            presenter?.onRootViewClicked()
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_player_search

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        contentAvailableItem = menu?.findItem(R.id.action_content_available)
        shareItem = menu?.findItem(R.id.action_share)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_content_available -> presenter?.onContentButtonClicked()
            R.id.action_share -> presenter?.onShareStatsButtonClicked(System.currentTimeMillis())
        }

        return super.onOptionsItemSelected(item)
    }

    override fun showPlayerFoundMessage(message: String) = ui {
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)

        ColoredSnackbar.confirm(snackbar).show()
    }

    override fun showError(message: String) = ui {
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)

        ColoredSnackbar.alertBold(snackbar).show()
    }

    override fun showDialog(title: String, message: String) = ui {
        if (!isFinishing) {
            val dialog = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setCancelable(false)
                .create()

            dialog.show()
        }
    }

    override fun hideSoftKeyboard() = ui {
        hideSoftKeyboard(rootView)
    }

    override fun showLoading() = ui {
        btn_send.visibility = INVISIBLE
        recyclerView.visibility = INVISIBLE
        tv_empty_case.visibility = INVISIBLE

        progressBar.visibility = VISIBLE
    }

    override fun hideLoading() = ui {
        btn_send.visibility = VISIBLE

        progressBar.visibility = GONE
    }

    override fun showEmptyCase() = ui {
        tv_empty_case.visibility = VISIBLE
        recyclerView.visibility = INVISIBLE
    }

    override fun hideEmptyCase() = ui {
        tv_empty_case.visibility = INVISIBLE
        recyclerView.visibility = VISIBLE
    }

    override fun fillPlayerAccount(account: String) = ui {
        et_username.setText(account)
    }

    override fun clearList() = ui {
        adapter?.clear()
    }

    override fun addMatch(match: Match) = ui {
        adapter?.add(match)

        adapter?.notifyDataSetChanged()
    }

    override fun onMatchClicked(match: Match) {
        presenter?.onMatchClicked(match)
    }

    override fun onLoadMoreClicked() {
        coroutineScope.launch {
            presenter?.onLoadMoreMatchesClicked()
        }
    }

    override fun onPlayerSeasonInfoClicked(playerSeasonInfo: PlayerSeasonInfo) {
        presenter?.onPlayerSeasonInfoClicked(playerSeasonInfo)
    }

    override fun addPlayerStatsRow(seasonInfo: PlayerSeasonInfo) = ui {
        adapter?.add(seasonInfo)

        adapter?.notifyDataSetChanged()
    }

    override fun addLoadMoreItem() = ui {
        adapter?.add(LoadMore("matches"))

        adapter?.notifyDataSetChanged()
    }

    override fun removeLoadMoreItem() = ui {
        adapter?.remove(LoadMore("matches"))

        adapter?.notifyDataSetChanged()
    }

    override fun hideContentAvailableButton() = ui {
        contentAvailableItem?.isVisible = false
    }

    override fun showContentAvailableButton() = ui {
        contentAvailableItem?.isVisible = true
    }

    override fun launchContentDetailScreen(content: Content) = ui {
        startActivity<ContentDetailActivity>(EXTRA_CONTENT_ID to content.id)
    }

    override fun showNoMatchesInSeasonMessage(message: String) = ui {
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)

        ColoredSnackbar.warningBold(snackbar).show()
    }

    override fun launchPlayerSeasonInfoScreen(player: Player, season: Season?) {
        startActivity<SeasonStatsDetailActivity>(
            EXTRA_PLAYER_ID to player.id,
            EXTRA_PLAYER_NAME to player.name,
            EXTRA_SEASON to (season?.id ?: "")
        )
    }

    override fun showShareButton() = ui {
        shareItem?.isVisible = true
    }

    override fun hideShareButton() = ui {
        shareItem?.isVisible = false
    }

    override fun takeScreenshot(path: String) = ui {
        takeAScreenshot(path)
    }

    override fun sharePlayerStatsNougat(screenshotPath: String) = ui {
        shareFileNougat(getString(R.string.share), screenshotPath)
    }

    override fun sharePlayerStatsPreNougat(screenshotPath: String) = ui {
        shareFilePreNougat(getString(R.string.share), screenshotPath)
    }
}
