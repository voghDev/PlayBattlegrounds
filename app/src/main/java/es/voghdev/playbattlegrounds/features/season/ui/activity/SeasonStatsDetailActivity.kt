package es.voghdev.playbattlegrounds.features.season.ui.activity

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.widget.TextView
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

            presenter?.onInitialData(SeasonStatsDetailPresenter.AndroidInitialData(intent))
        }
    }

    fun TextView.highlightCharsAfter(separator: String = ": ", highlightColorResId: Int) {
        val start = maxOf(1, text.indexOf(": "))
        val end = text.length

        text = SpannableString(text).apply {
            setSpan(ForegroundColorSpan(
                ContextCompat.getColor(applicationContext, highlightColorResId)),
                    start,
                    end,
                    SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(RelativeSizeSpan(1.2f),
                    start,
                    end,
                    SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    override fun showSoloKDR(text: String, statColorResId: Int) = ui {
        tv_solo_kdr.text = text
        tv_solo_kdr.highlightCharsAfter(": ", statColorResId)
    }

    override fun showSoloFPPKDR(text: String, statColorResId: Int) = ui {
        tv_solo_fpp_kdr.text = text
        tv_solo_fpp_kdr.highlightCharsAfter(": ", statColorResId)
    }

    override fun showDuoKDR(text: String, statColorResId: Int) = ui {
        tv_duo_kdr.text = text
        tv_duo_kdr.highlightCharsAfter(": ", statColorResId)
    }

    override fun showDuoFPPKDR(text: String, statColorResId: Int) = ui {
        tv_duo_fpp_kdr.text = text
        tv_duo_fpp_kdr.highlightCharsAfter(": ", statColorResId)
    }

    override fun showSquadKDR(text: String, statColorResId: Int) = ui {
        tv_squad_kdr.text = text
        tv_squad_kdr.highlightCharsAfter(": ", statColorResId)
    }

    override fun showSquadFPPKDR(text: String, statColorResId: Int) = ui {
        tv_squad_fpp_kdr.text = text
        tv_squad_fpp_kdr.highlightCharsAfter(": ", statColorResId)
    }

    override fun showSoloSummary(text: String) = ui {
        tv_solo_kills.text = text
    }

    override fun showSoloFPPSummary(text: String) = ui {
        tv_solo_fpp_kills.text = text
    }

    override fun showDuoSummary(text: String) = ui {
        tv_duo_kills.text = text
    }

    override fun showDuoFPPSummary(text: String) = ui {
        tv_duo_fpp_kills.text = text
    }

    override fun showSquadSummary(text: String) = ui {
        tv_squad_kills.text = text
    }

    override fun showSquadFPPSummary(text: String) = ui {
        tv_squad_fpp_kills.text = text
    }

    override fun showSoloRating(text: String, statColorResId: Int) = ui {
        tv_solo_rating.text = text
        tv_solo_rating.highlightCharsAfter(": ", statColorResId)
    }

    override fun showSoloFPPRating(text: String, statColorResId: Int) = ui {
        tv_solo_fpp_rating.text = text
        tv_solo_fpp_rating.highlightCharsAfter(": ", statColorResId)
    }

    override fun showDuoRating(text: String, statColorResId: Int) = ui {
        tv_duo_rating.text = text
        tv_duo_rating.highlightCharsAfter(": ", statColorResId)
    }

    override fun showDuoFPPRating(text: String, statColorResId: Int) = ui {
        tv_duo_fpp_rating.text = text
        tv_duo_fpp_rating.highlightCharsAfter(": ", statColorResId)
    }

    override fun showSquadRating(text: String, statColorResId: Int) = ui {
        tv_squad_rating.text = text
        tv_squad_rating.highlightCharsAfter(": ", statColorResId)
    }

    override fun showSquadFPPRating(text: String, statColorResId: Int) = ui {
        tv_squad_fpp_rating.text = text
        tv_squad_fpp_rating.highlightCharsAfter(": ", statColorResId)
    }

    override fun configureToolbar() = ui { supportActionBar?.setDisplayHomeAsUpEnabled(true) }

    override fun getLayoutId(): Int = R.layout.activity_season_stats_detail
}
