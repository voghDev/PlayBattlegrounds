package es.voghdev.playbattlegrounds.features.season.ui.activity

import android.os.Bundle
import com.appandweb.peep.ui.activity.BaseActivity
import es.voghdev.playbattlegrounds.R
import es.voghdev.playbattlegrounds.common.asApp
import es.voghdev.playbattlegrounds.common.reslocator.AndroidResLocator
import es.voghdev.playbattlegrounds.features.players.PlayerRepository
import es.voghdev.playbattlegrounds.features.season.ui.presenter.SeasonStatsDetailPresenter
import es.voghdev.playbattlegrounds.ui
import kotlinx.android.synthetic.main.activity_season_stats_detail.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class SeasonStatsDetailActivity : BaseActivity(), KodeinAware, SeasonStatsDetailPresenter.MVPView, SeasonStatsDetailPresenter.Navigator {
    override val kodein: Kodein by lazy { applicationContext.asApp().kodein }

    var presenter: SeasonStatsDetailPresenter? = null
    val playerRepository: PlayerRepository by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = SeasonStatsDetailPresenter(AndroidResLocator(this), playerRepository)
        presenter?.view = this
        presenter?.navigator = this

        launch(CommonPool) {
            presenter?.initialize()
        }
    }

    override fun showSoloKDR(text: String) = ui {
        tv_solo_kdr.text = text
    }

    override fun showSoloFPPKDR(text: String) = ui {
        tv_solo_fpp_kdr.text = text
    }

    override fun showDuoKDR(text: String) = ui {
        tv_duo_kdr.text = text
    }

    override fun showDuoFPPKDR(text: String) = ui {
        tv_duo_fpp_kdr.text = text
    }

    override fun showSquadKDR(text: String) = ui {
        tv_squad_kdr.text = text
    }

    override fun showSquadFPPKDR(text: String) = ui {
        tv_squad_fpp_kdr.text = text
    }

    override fun showSoloRating(text: String) = ui {
        tv_solo_rating.text = text
    }

    override fun showSoloFPPRating(text: String) = ui {
        tv_solo_fpp_rating.text = text
    }

    override fun showDuoRating(text: String) = ui {
        tv_duo_rating.text = text
    }

    override fun showDuoFPPRating(text: String) = ui {
        tv_duo_fpp_rating.text = text
    }

    override fun showSquadRating(text: String) = ui {
        tv_squad_rating.text = text
    }

    override fun showSquadFPPRating(text: String) = ui {
        tv_squad_fpp_rating.text = text
    }

    override fun configureToolbar() = ui { supportActionBar?.setDisplayHomeAsUpEnabled(true) }

    override fun getLayoutId(): Int = R.layout.activity_season_stats_detail
}
