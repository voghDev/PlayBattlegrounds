package es.voghdev.playbattlegrounds.features.season.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.core.content.ContextCompat
import es.voghdev.playbattlegrounds.R
import es.voghdev.playbattlegrounds.common.BaseActivity
import es.voghdev.playbattlegrounds.common.EXTRA_PLAYER_ID
import es.voghdev.playbattlegrounds.common.EXTRA_PLAYER_NAME
import es.voghdev.playbattlegrounds.common.EXTRA_SEASON
import es.voghdev.playbattlegrounds.common.asApp
import es.voghdev.playbattlegrounds.features.players.PlayerRepository
import es.voghdev.playbattlegrounds.features.season.ui.presenter.SeasonStatsDetailPresenter
import es.voghdev.playbattlegrounds.features.share.GetImagesPathAndroidDataSource
import es.voghdev.playbattlegrounds.shareFileNougat
import es.voghdev.playbattlegrounds.shareFilePreNougat
import es.voghdev.playbattlegrounds.takeAScreenshot
import es.voghdev.playbattlegrounds.ui
import kotlinx.android.synthetic.main.activity_season_stats_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class SeasonStatsDetailActivity : BaseActivity(), KodeinAware, SeasonStatsDetailPresenter.MVPView, SeasonStatsDetailPresenter.Navigator {
    override val kodein: Kodein by lazy { applicationContext.asApp().kodein }

    lateinit var presenter: SeasonStatsDetailPresenter
    val playerRepository: PlayerRepository by instance()
    var shareItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = SeasonStatsDetailPresenter(
            Dispatchers.IO,
            playerRepository,
            GetImagesPathAndroidDataSource(applicationContext))
        presenter.view = this
        presenter.navigator = this

        coroutineScope.launch {
            presenter.initialize()

            presenter.onInitialData(AndroidInitialData(intent))
        }

        shareButton.setOnClickListener {
            presenter.onShareSeasonStatsButtonClicked(millis())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_screens, menu)
        shareItem = menu?.findItem(R.id.action_share)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_share -> presenter.onShareSeasonStatsButtonClicked(millis())
        }

        return super.onOptionsItemSelected(item)
    }

    private fun millis() = System.currentTimeMillis()

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

    override fun showShareButton() = ui {
        shareItem?.isVisible = true

        rootView.transitionToEnd()
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

    override fun render(state: SeasonStatsDetailPresenter.ViewState) {
        state as SeasonStatsDetailPresenter.ViewState.Success

        soloKDRTextView.text = state.soloKDR
        soloKDRTextView.highlightCharsAfter(": ", state.soloColor)
        soloKillsTextView.text = state.soloSummary

        soloFppKDRTextView.text = state.soloFppKDR
        soloFppKDRTextView.highlightCharsAfter(": ", state.soloFppColor)
        soloFppKillsTextView.text = state.soloFppSummary

        duoKdrTextView.text = state.duoKDR
        duoKdrTextView.highlightCharsAfter(": ", state.duoColor)
        duoKillsTextView.text = state.duoSummary

        duoFppKDRTextView.text = state.duoFppKDR
        duoFppKDRTextView.highlightCharsAfter(": ", state.duoFppColor)
        duoFppKillsTextView.text = state.duoFppSummary

        squadKDRTextView.text = state.squadKDR
        squadKDRTextView.highlightCharsAfter(": ", state.squadColor)
        squadKillsTextView.text = state.squadSummary

        squadFppKDRTextView.text = state.squadFppKDR
        squadFppKDRTextView.highlightCharsAfter(": ", state.squadFppColor)
        squadFppKillsTextView.text = state.squadFppSummary
    }

    override fun configureToolbar() = ui { supportActionBar?.setDisplayHomeAsUpEnabled(true) }

    class AndroidInitialData(val intent: Intent?) : SeasonStatsDetailPresenter.InitialData {
        override fun getPlayerId(): String =
            intent?.getStringExtra(EXTRA_PLAYER_ID) ?: ""

        override fun getPlayerName(): String =
            intent?.getStringExtra(EXTRA_PLAYER_NAME) ?: ""

        override fun getSeason(): String =
            intent?.getStringExtra(EXTRA_SEASON) ?: ""

        override fun getSdkVersion(): Int = Build.VERSION.SDK_INT
    }

    override fun getLayoutId(): Int = R.layout.activity_season_stats_detail
}
