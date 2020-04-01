package es.voghdev.playbattlegrounds.features.players.ui.presenter

import es.voghdev.playbattlegrounds.common.reslocator.ResLocator
import es.voghdev.playbattlegrounds.features.players.PlayerRepository
import es.voghdev.playbattlegrounds.features.players.usecase.GetContentById
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ContentDetailPresenterTest {
    @Mock
    lateinit var mockResLocator: ResLocator

    @Mock
    lateinit var mockGetContentById: GetContentById

    @Mock
    lateinit var mockNavigator: ContentDetailPresenter.Navigator

    @Mock
    lateinit var mockView: ContentDetailPresenter.MVPView

    @Mock
    lateinit var mockPlayerRepository: PlayerRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    private fun createMockedPresenter(): ContentDetailPresenter {
        val presenter = ContentDetailPresenter(Dispatchers.Main, mockPlayerRepository)
        presenter.view = mockView
        presenter.navigator = mockNavigator
        return presenter
    }
}
