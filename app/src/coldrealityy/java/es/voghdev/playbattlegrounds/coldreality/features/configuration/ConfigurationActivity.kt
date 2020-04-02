package es.voghdev.playbattlegrounds.coldreality.features.configuration

import android.os.Bundle
import es.voghdev.playbattlegrounds.R
import es.voghdev.playbattlegrounds.coldreality.features.configuration.datasource.GetConfigurationApiDataSource
import es.voghdev.playbattlegrounds.common.BaseActivity
import es.voghdev.playbattlegrounds.common.asApp
import es.voghdev.playbattlegrounds.common.reslocator.ResLocator
import es.voghdev.playbattlegrounds.features.onboarding.ui.IntroActivity
import es.voghdev.playbattlegrounds.features.players.PlayerRepository
import es.voghdev.playbattlegrounds.startActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
            Dispatchers.IO,
            GetConfigurationApiDataSource(),
            playerRepository
        )
        presenter?.view = this
        presenter?.navigator = this

        coroutineScope.launch {
            presenter?.initialize()
        }
    }

    override fun launchOnboardingScreen() {
        startActivity<IntroActivity>()
    }

    override fun close() = finish()

    override fun getLayoutId(): Int = R.layout.activity_configuration
}
