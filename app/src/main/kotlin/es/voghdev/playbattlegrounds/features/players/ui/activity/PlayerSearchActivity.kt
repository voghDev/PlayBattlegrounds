package es.voghdev.playbattlegrounds.features.players.ui.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.View.*
import com.appandweb.peep.ui.activity.BaseActivity
import com.pedrogomez.renderers.RVRendererAdapter
import com.pedrogomez.renderers.RendererBuilder
import es.voghdev.playbattlegrounds.R
import es.voghdev.playbattlegrounds.common.asApp
import es.voghdev.playbattlegrounds.common.reslocator.ResLocator
import es.voghdev.playbattlegrounds.common.ui.ColoredSnackbar
import es.voghdev.playbattlegrounds.features.matches.Match
import es.voghdev.playbattlegrounds.features.matches.ui.MatchRenderer
import es.voghdev.playbattlegrounds.features.matches.usecase.GetMatchById
import es.voghdev.playbattlegrounds.features.players.ui.presenter.PlayerSearchPresenter
import es.voghdev.playbattlegrounds.features.players.usecase.GetPlayerByName
import es.voghdev.playbattlegrounds.hideSoftKeyboard
import es.voghdev.playbattlegrounds.ui
import kotlinx.android.synthetic.main.activity_player_search.*
import kotlinx.coroutines.experimental.runBlocking
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class PlayerSearchActivity : BaseActivity(), KodeinAware, PlayerSearchPresenter.MVPView, PlayerSearchPresenter.Navigator, MatchRenderer.OnRowClicked {
    override val kodein: Kodein by lazy { applicationContext.asApp().kodein }

    val getPlayerByNameDataSource: GetPlayerByName by instance()
    val getMatchByIdDataSource: GetMatchById by instance()
    val resLocator: ResLocator by instance()
    var adapter: RVRendererAdapter<Match>? = null

    var presenter: PlayerSearchPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val renderer = MatchRenderer(this)
        val rendererBuilder = RendererBuilder<Match>(renderer)
        adapter = RVRendererAdapter<Match>(rendererBuilder)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        presenter = PlayerSearchPresenter(resLocator, getPlayerByNameDataSource, getMatchByIdDataSource)
        presenter?.view = this
        presenter?.navigator = this

        runBlocking {
            presenter?.initialize()
        }

        btn_send.setOnClickListener {
            presenter?.onSendButtonClicked(et_username.text.toString().trim())
        }

        rootView.setOnClickListener {
            presenter?.onRootViewClicked()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_player_search
    }

    override fun showPlayerName(name: String) = ui {
        tv_player_name.text = name
        tv_player_name.visibility = VISIBLE
    }

    override fun showLastMatchInfo(text: String) = ui {
        tv_last_match.text = text
        tv_last_match.visibility = VISIBLE
    }

    override fun showError(message: String) = ui {
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)

        ColoredSnackbar.alertBold(snackbar).show()
    }

    override fun hideSoftKeyboard() = ui {
        hideSoftKeyboard(rootView)
    }

    override fun showLoading() = ui {
        btn_send.visibility = INVISIBLE
        tv_last_match.visibility = INVISIBLE
        tv_player_name.visibility = INVISIBLE

        progressBar.visibility = VISIBLE
    }

    override fun hideLoading() = ui {
        btn_send.visibility = VISIBLE

        progressBar.visibility = GONE
    }

    override fun clearList() = ui {
        adapter?.clear()
    }

    override fun addMatch(match: Match) = ui {
        adapter?.add(match)

        adapter?.notifyDataSetChanged()
    }

    override fun onMatchClicked(match: Match) {
        match.date
    }
}
