package es.voghdev.playbattlegrounds.features.players.ui.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import com.appandweb.peep.ui.activity.BaseActivity
import es.voghdev.playbattlegrounds.R
import es.voghdev.playbattlegrounds.common.asApp
import es.voghdev.playbattlegrounds.common.reslocator.ResLocator
import es.voghdev.playbattlegrounds.common.ui.ColoredSnackbar
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

class PlayerSearchActivity : BaseActivity(), KodeinAware, PlayerSearchPresenter.MVPView, PlayerSearchPresenter.Navigator {
    override val kodein: Kodein by lazy { applicationContext.asApp().kodein }

    val getPlayerByNameDataSource: GetPlayerByName by instance()
    val getMatchByIdDataSource: GetMatchById by instance()
    val resLocator: ResLocator by instance()

    var presenter: PlayerSearchPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
    }

    override fun showLastMatchInfo(text: String) = ui {
        tv_last_match.text = text
    }

    override fun showError(message: String) = ui {
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)

        ColoredSnackbar.alertBold(snackbar).show()
    }

    override fun hideSoftKeyboard() = ui {
        hideSoftKeyboard(rootView)
    }
}
