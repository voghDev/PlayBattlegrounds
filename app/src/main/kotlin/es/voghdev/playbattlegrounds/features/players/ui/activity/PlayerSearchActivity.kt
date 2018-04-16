package es.voghdev.playbattlegrounds.features.players.ui.activity

import android.os.Bundle
import com.appandweb.peep.ui.activity.BaseActivity
import es.voghdev.playbattlegrounds.R
import es.voghdev.playbattlegrounds.common.reslocator.AndroidResLocator
import es.voghdev.playbattlegrounds.features.players.ui.presenter.PlayerSearchPresenter
import kotlinx.android.synthetic.main.activity_player_search.*
import kotlinx.coroutines.experimental.runBlocking

class PlayerSearchActivity : BaseActivity(), PlayerSearchPresenter.MVPView, PlayerSearchPresenter.Navigator {
    var presenter: PlayerSearchPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = PlayerSearchPresenter(AndroidResLocator(this))
        presenter?.view = this
        presenter?.navigator = this

        runBlocking {
            presenter?.initialize()
        }

        rootView.setOnClickListener {
            presenter.onSendButtonClicked("some_player")
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_player_search
    }
}
