package es.voghdev.playbattlegrounds.features.players.ui.activity

import android.os.Bundle
import com.appandweb.peep.ui.activity.BaseActivity
import es.voghdev.playbattlegrounds.R
import es.voghdev.playbattlegrounds.common.asApp
import es.voghdev.playbattlegrounds.features.players.ui.presenter.ContentDetailInitialData
import es.voghdev.playbattlegrounds.features.players.ui.presenter.ContentDetailPresenter
import es.voghdev.playbattlegrounds.features.players.usecase.GetContentById
import es.voghdev.playbattlegrounds.ui
import kotlinx.android.synthetic.main.activity_content_detail.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class ContentDetailActivity : BaseActivity(), KodeinAware, ContentDetailPresenter.MVPView, ContentDetailPresenter.Navigator {
    override val kodein: Kodein by lazy { applicationContext.asApp().kodein }

    var presenter: ContentDetailPresenter? = null
    val getContentById: GetContentById by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = ContentDetailPresenter(getContentById)
        presenter?.view = this
        presenter?.navigator = this

        launch(CommonPool) {
            presenter?.initialize()

            presenter?.onInitialData(ContentDetailInitialData(intent))
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_content_detail

    override fun configureToolbar() = ui { supportActionBar?.setDisplayHomeAsUpEnabled(true) }

    override fun showContentTitle(title: String) = ui { tv_title.text = title }

    override fun showContentText(text: String) = ui { tv_text.text = text }

    override fun showButtonText(text: String) = ui { btn_read_more.text = text }

    override fun close() = finish()
}
