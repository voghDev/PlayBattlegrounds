package es.voghdev.playbattlegrounds.features.players.ui.presenter

import es.voghdev.playbattlegrounds.common.reslocator.ResLocator
import es.voghdev.playbattlegrounds.features.matches.MatchRepository
import es.voghdev.playbattlegrounds.features.matches.usecase.GetMatchById
import es.voghdev.playbattlegrounds.features.matches.usecase.InsertMatch
import es.voghdev.playbattlegrounds.features.onboarding.usecase.GetPlayerAccount
import es.voghdev.playbattlegrounds.features.players.PlayerRepository
import es.voghdev.playbattlegrounds.features.players.usecase.GetPlayerById
import es.voghdev.playbattlegrounds.features.players.usecase.GetPlayerByName
import es.voghdev.playbattlegrounds.features.season.usecase.GetCurrentSeason
import es.voghdev.playbattlegrounds.features.season.usecase.GetPlayerSeasonInfo
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PlayerSearchPresenterTest {

    @Mock
    lateinit var mockResLocator: ResLocator

    @Mock
    lateinit var mockGetPlayerById: GetPlayerById

    @Mock
    lateinit var mockGetPlayerByName: GetPlayerByName

    @Mock
    lateinit var mockGetMatchByIdApi: GetMatchById

    @Mock
    lateinit var mockGetMatchByIdDB: GetMatchById

    @Mock
    lateinit var mockInsertMatch: InsertMatch

    @Mock
    lateinit var mockGetPlayerAccount: GetPlayerAccount

    @Mock
    lateinit var mockGetCurrentSeason: GetCurrentSeason

    @Mock
    lateinit var mockGetPlayerSeasonInfo: GetPlayerSeasonInfo

    @Mock
    lateinit var mockNavigator: PlayerSearchPresenter.Navigator

    @Mock
    lateinit var mockView: PlayerSearchPresenter.MVPView

    lateinit var presenter: PlayerSearchPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = createPresenterWithMocks()
    }

    private fun createPresenterWithMocks(): PlayerSearchPresenter {
        val presenter = PlayerSearchPresenter(mockResLocator,
                PlayerRepository(
                        mockGetPlayerById,
                        mockGetPlayerByName,
                        "Too many requests"
                ),
                MatchRepository(
                        mockGetMatchByIdApi,
                        mockGetMatchByIdDB,
                        mockInsertMatch),
                mockGetPlayerAccount,
                mockGetCurrentSeason,
                mockGetPlayerSeasonInfo)
        presenter.view = mockView
        presenter.navigator = mockNavigator
        return presenter
    }
}
