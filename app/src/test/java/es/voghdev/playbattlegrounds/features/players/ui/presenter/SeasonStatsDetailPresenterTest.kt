package es.voghdev.playbattlegrounds.features.players.ui.presenter

import arrow.core.Either
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import es.voghdev.playbattlegrounds.common.reslocator.ResLocator
import es.voghdev.playbattlegrounds.features.players.PlayerRepository
import es.voghdev.playbattlegrounds.features.players.model.Player
import es.voghdev.playbattlegrounds.features.season.Season
import es.voghdev.playbattlegrounds.features.season.model.PlayerSeasonInfo
import es.voghdev.playbattlegrounds.features.season.ui.presenter.SeasonStatsDetailPresenter
import es.voghdev.playbattlegrounds.features.share.GetImagesPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SeasonStatsDetailPresenterTest {

    @Mock
    lateinit var mockResLocator: ResLocator

    @Mock
    lateinit var mockGetImagesPath: GetImagesPath

    @Mock
    lateinit var mockNavigator: SeasonStatsDetailPresenter.Navigator

    @Mock
    lateinit var mockView: SeasonStatsDetailPresenter.MVPView

    @Mock
    lateinit var mockPlayerRepository: PlayerRepository

    private lateinit var presenter: SeasonStatsDetailPresenter

    val aPlayerSeasonInfo = mock<PlayerSeasonInfo> { on }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = createMockedPresenter()

        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @Test
    fun `should show SoloKDR for a player with Season Stats`() = runBlockingTest {
        val data = object : SeasonStatsDetailPresenter.InitialData {
            override fun getPlayerId(): String = "123482348"
            override fun getPlayerName(): String = "DanucD"
            override fun getSeason(): String = "season-9"
            override fun getSdkVersion(): Int = 21
        }

        val captor = argumentCaptor<SeasonStatsDetailPresenter.ViewState.Success>()

        whenever(mockPlayerRepository.getPlayerSeasonInfo(
            anyPlayer(),
            anySeason(),
            anyLong())
        )
            .thenReturn(Either.Right(aPlayerSeasonInfo))

        launch {
            presenter.onInitialData(data)
        }

        verify(mockView).render(captor.capture())
    }

    private fun anyPlayer(): Player = any()
    private fun anySeason(): Season = any()

    private fun createMockedPresenter(): SeasonStatsDetailPresenter {
        val presenter = SeasonStatsDetailPresenter(
            Dispatchers.Main,
            mockPlayerRepository,
            mockGetImagesPath)
        presenter.view = mockView
        presenter.navigator = mockNavigator
        return presenter
    }
}
