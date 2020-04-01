package es.voghdev.playbattlegrounds.season1.features.tops

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedrogomez.renderers.RVRendererAdapter
import com.pedrogomez.renderers.RendererBuilder
import es.voghdev.playbattlegrounds.R
import es.voghdev.playbattlegrounds.common.BaseActivity
import es.voghdev.playbattlegrounds.common.asApp
import es.voghdev.playbattlegrounds.common.reslocator.ResLocator
import es.voghdev.playbattlegrounds.common.ui.ListEntity
import es.voghdev.playbattlegrounds.features.players.ui.activity.PlayerSearchActivity
import es.voghdev.playbattlegrounds.features.season.usecase.GetSeasons
import es.voghdev.playbattlegrounds.features.season.usecase.SetCurrentSeason
import es.voghdev.playbattlegrounds.season1.common.GetPicturesPathAndroidImpl
import es.voghdev.playbattlegrounds.season1.common.asset.CopyAssetThreadImpl
import es.voghdev.playbattlegrounds.season1.features.tops.TopPlayerRenderer.OnRowClicked
import es.voghdev.playbattlegrounds.season1.features.tops.api.GetTopPlayersApiDataSource
import es.voghdev.playbattlegrounds.season1.features.tops.model.TopPlayer
import es.voghdev.playbattlegrounds.season1.features.tops.model.Whitespace
import es.voghdev.playbattlegrounds.season1.features.tops.renderer.WhitespaceRenderer
import es.voghdev.playbattlegrounds.ui
import kotlinx.android.synthetic.topplayers.activity_top_players.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.startActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class TopPlayersActivity : BaseActivity(), KodeinAware, TopPlayersPresenter.MVPView, TopPlayersPresenter.Navigator, OnRowClicked {
    override val kodein: Kodein by lazy { applicationContext.asApp().kodein }

    val resLocator: ResLocator by instance()
    val getSeasons: GetSeasons by instance()
    val setCurrentSeason: SetCurrentSeason by instance()
    var adapter: RVRendererAdapter<ListEntity>? = null

    var presenter: TopPlayersPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val renderer = TopPlayerRenderer(this)
        val rendererBuilder = RendererBuilder<ListEntity>()
            .bind(TopPlayer::class.java, renderer)
            .bind(Whitespace::class.java, WhitespaceRenderer())
        adapter = RVRendererAdapter<ListEntity>(rendererBuilder)

        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        presenter = TopPlayersPresenter(
            resLocator,
            GetTopPlayersApiDataSource(),
            CopyAssetThreadImpl(applicationContext),
            GetPicturesPathAndroidImpl(applicationContext),
            getSeasons,
            setCurrentSeason
        )
        presenter?.view = this
        presenter?.navigator = this

        launch(CommonPool) {
            presenter?.initialize()
        }
    }

    override fun addPlayer(player: TopPlayer) = ui {
        adapter?.add(player)

        adapter?.notifyDataSetChanged()
    }

    override fun onTopPlayerClicked(topPlayer: TopPlayer) {
        presenter?.onTopPlayerClicked(topPlayer)
    }

    override fun launchPlayerSearchScreenForPlayer(topPlayer: TopPlayer) {
        startActivity<PlayerSearchActivity>("playerName" to topPlayer.player.name, "additionalContents" to false)
    }

    override fun getLayoutId(): Int = R.layout.activity_top_players
}