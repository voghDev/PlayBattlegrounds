package es.voghdev.playbattlegrounds.features.players.ui.presenter

import es.voghdev.playbattlegrounds.common.reslocator.ResLocator
import es.voghdev.playbattlegrounds.features.players.PlayerRepository
import es.voghdev.playbattlegrounds.features.season.ui.presenter.SeasonStatsDetailPresenter
import es.voghdev.playbattlegrounds.features.share.GetImagesPath
import org.junit.Before
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

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    private fun createMockedPresenter(): SeasonStatsDetailPresenter {
        val presenter = SeasonStatsDetailPresenter(
            mockResLocator,
            mockPlayerRepository,
            mockGetImagesPath)
        presenter.view = mockView
        presenter.navigator = mockNavigator
        return presenter
    }
}
