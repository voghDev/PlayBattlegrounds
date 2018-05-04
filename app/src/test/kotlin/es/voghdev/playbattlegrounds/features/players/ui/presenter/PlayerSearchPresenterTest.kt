package es.voghdev.playbattlegrounds.features.players.ui.presenter

import es.voghdev.playbattlegrounds.common.reslocator.ResLocator
import es.voghdev.playbattlegrounds.features.matches.usecase.GetMatchById
import es.voghdev.playbattlegrounds.features.players.usecase.GetPlayerByName
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PlayerSearchPresenterTest {

    @Mock lateinit var mockResLocator: ResLocator

    @Mock lateinit var mockGetPlayerByName: GetPlayerByName

    @Mock lateinit var mockGetMatchById : GetMatchById

    @Mock lateinit var mockNavigator: PlayerSearchPresenter.Navigator

    @Mock lateinit var mockView: PlayerSearchPresenter.MVPView

    lateinit var presenter: PlayerSearchPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = createPresenterWithMocks()
    }

    private fun createPresenterWithMocks(): PlayerSearchPresenter {
        val presenter = PlayerSearchPresenter(mockResLocator, mockGetPlayerByName, mockGetMatchById)
        presenter.view = mockView
        presenter.navigator = mockNavigator
        return presenter
    }
}
