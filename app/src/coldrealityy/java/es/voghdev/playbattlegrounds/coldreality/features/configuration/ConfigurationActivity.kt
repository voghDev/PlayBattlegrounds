package es.voghdev.playbattlegrounds.coldreality.features.configuration

import android.os.Bundle
import es.voghdev.playbattlegrounds.R
import es.voghdev.playbattlegrounds.coldreality.features.configuration.datasource.GetConfigurationApiDataSource
import es.voghdev.playbattlegrounds.common.BaseActivity
import es.voghdev.playbattlegrounds.common.asApp
import es.voghdev.playbattlegrounds.common.reslocator.ResLocator
import es.voghdev.playbattlegrounds.features.onboarding.ui.IntroActivity
import es.voghdev.playbattlegrounds.features.players.PlayerRepository
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.startActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class ConfigurationActivity : BaseActivity(), KodeinAware, ConfigurationPresenter.MVPView, ConfigurationPresenter.Navigator {
    override val kodein: Kodein by lazy { applicationContext.asApp().kodein }

    val resLocator: ResLocator by instance()
    var presenter: ConfigurationPresenter? = null
    val playerRepository: PlayerRepository by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = ConfigurationPresenter(
            resLocator,
            GetConfigurationApiDataSource(),
            playerRepository
        )
        presenter?.view = this
        presenter?.navigator = this

        launch(CommonPool) {
            presenter?.initialize()
        }
    }

    override fun launchOnboardingScreen() {
        startActivity<IntroActivity>()
    }

    override fun close() = finish()

    override fun getLayoutId(): Int = R.layout.activity_configuration
}
