package es.voghdev.playbattlegrounds.features.players.ui.presenter

import es.voghdev.playbattlegrounds.common.reslocator.ResLocator
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PlayerSearchPresenterTest() {
    @Mock lateinit var mockResLocator: ResLocator

    @Mock lateinit var mockNavigator: PlayerSearchPresenter.Navigator

    @Mock lateinit var mockView: PlayerSearchPresenter.MVPView

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    private fun createMockedPresenter(): PlayerSearchPresenter {
        val presenter = PlayerSearchPresenter(mockResLocator)
        presenter.view = mockView
        presenter.navigator = mockNavigator
        return presenter
    }
}
