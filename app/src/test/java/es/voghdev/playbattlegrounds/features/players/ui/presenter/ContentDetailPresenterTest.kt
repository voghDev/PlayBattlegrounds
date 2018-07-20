package es.voghdev.playbattlegrounds.features.players.ui.presenter

import es.voghdev.playbattlegrounds.common.reslocator.ResLocator
import es.voghdev.playbattlegrounds.features.players.usecase.GetContentById
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

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    private fun createMockedPresenter(): ContentDetailPresenter {
        val presenter = ContentDetailPresenter(mockGetContentById)
        presenter.view = mockView
        presenter.navigator = mockNavigator
        return presenter
    }
}
