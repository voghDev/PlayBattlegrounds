package es.voghdev.playbattlegrounds.features.season.ui.activity

import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import es.voghdev.playbattlegrounds.R
import es.voghdev.playbattlegrounds.common.BaseActivity
import es.voghdev.playbattlegrounds.common.asApp
import es.voghdev.playbattlegrounds.common.reslocator.AndroidResLocator
import es.voghdev.playbattlegrounds.features.players.PlayerRepository
import es.voghdev.playbattlegrounds.features.season.ui.presenter.SeasonStatsDetailPresenter
import es.voghdev.playbattlegrounds.features.share.GetImagesPathAndroidDataSource
import es.voghdev.playbattlegrounds.shareFileNougat
import es.voghdev.playbattlegrounds.shareFilePreNougat
import es.voghdev.playbattlegrounds.takeAScreenshot
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
    var shareItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = SeasonStatsDetailPresenter(
            AndroidResLocator(this),
            playerRepository,
            GetImagesPathAndroidDataSource(applicationContext))
        presenter?.view = this
        presenter?.navigator = this

        launch(CommonPool) {
            presenter?.initialize()

            presenter?.onInitialData(SeasonStatsDetailPresenter.AndroidInitialData(intent))

            presenter?.onSdkVersionReceived(Build.VERSION.SDK_INT)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_screens, menu)
        shareItem = menu?.findItem(R.id.action_share)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_share -> presenter?.onShareSeasonStatsButtonClicked(System.currentTimeMillis())
        }

        return super.onOptionsItemSelected(item)
    }

    fun TextView.highlightCharsAfter(separator: String = ": ", highlightColorResId: Int) {
        val start = maxOf(1, text.indexOf(separator))
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

    override fun showToolbarTitle(title: String) = ui { supportActionBar?.title = title }

    override fun configureToolbar() = ui { supportActionBar?.setDisplayHomeAsUpEnabled(true) }

    override fun getLayoutId(): Int = R.layout.activity_season_stats_detail
}
