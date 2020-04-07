package es.voghdev.playbattlegrounds.features.players.ui.activity

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import es.voghdev.playbattlegrounds.R
import es.voghdev.playbattlegrounds.common.BaseActivity
import es.voghdev.playbattlegrounds.common.asApp
import es.voghdev.playbattlegrounds.features.players.PlayerRepository
import es.voghdev.playbattlegrounds.features.players.ui.presenter.ContentDetailInitialData
import es.voghdev.playbattlegrounds.features.players.ui.presenter.ContentDetailPresenter
import es.voghdev.playbattlegrounds.ui
import kotlinx.android.synthetic.main.activity_content_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class ContentDetailActivity : BaseActivity(), KodeinAware, ContentDetailPresenter.MVPView, ContentDetailPresenter.Navigator {
    override val kodein: Kodein by lazy { applicationContext.asApp().kodein }

    private lateinit var presenter: ContentDetailPresenter
    private val playerRepository: PlayerRepository by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = ContentDetailPresenter(Dispatchers.IO, playerRepository)
        presenter.view = this
        presenter.navigator = this

        coroutineScope.launch {
            presenter.initialize()

            presenter.onInitialData(ContentDetailInitialData(intent))
        }

        btn_read_more.setOnClickListener {
            presenter.onLinkButtonClicked()
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_content_detail

    override fun configureToolbar() = ui { supportActionBar?.setDisplayHomeAsUpEnabled(true) }

    override fun showContentTitle(title: String) = ui { tv_title.text = title }

    override fun showContentText(text: String) = ui { tv_text.text = text }

    override fun showButtonText(text: String) = ui { btn_read_more.text = text }

    override fun close() = finish()

    override fun launchBrowser(link: String) =
        startActivity(Intent(ACTION_VIEW).apply { data = Uri.parse(link) })
}
