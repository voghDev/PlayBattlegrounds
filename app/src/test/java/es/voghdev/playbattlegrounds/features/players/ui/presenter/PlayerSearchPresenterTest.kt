package es.voghdev.playbattlegrounds.features.players.ui.presenter

import arrow.core.Either
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import es.voghdev.playbattlegrounds.common.reslocator.ResLocator
import es.voghdev.playbattlegrounds.features.matches.Match
import es.voghdev.playbattlegrounds.features.matches.MatchRepository
import es.voghdev.playbattlegrounds.features.onboarding.model.Region
import es.voghdev.playbattlegrounds.features.onboarding.usecase.GetPlayerAccount
import es.voghdev.playbattlegrounds.features.onboarding.usecase.GetPlayerRegion
import es.voghdev.playbattlegrounds.features.players.PlayerRepository
import es.voghdev.playbattlegrounds.features.players.model.Player
import es.voghdev.playbattlegrounds.features.players.usecase.IsContentAvailableForPlayer
import es.voghdev.playbattlegrounds.features.season.usecase.GetCurrentSeason
import es.voghdev.playbattlegrounds.features.season.usecase.GetPlayerSeasonInfo
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PlayerSearchPresenterTest {

    @Mock
    lateinit var mockResLocator: ResLocator

    @Mock
    lateinit var mockGetPlayerAccount: GetPlayerAccount

    @Mock
    lateinit var mockGetCurrentSeason: GetCurrentSeason

    @Mock
    lateinit var mockGetPlayerSeasonInfo: GetPlayerSeasonInfo

    @Mock
    lateinit var mockNavigator: PlayerSearchPresenter.Navigator

    @Mock
    lateinit var mockPlayerRepository: PlayerRepository

    @Mock
    lateinit var mockMatchRepository: MatchRepository

    @Mock
    lateinit var mockIsContentAvailableForPlayer: IsContentAvailableForPlayer

    @Mock
    lateinit var mockGetPlayerRegion: GetPlayerRegion

    @Mock
    lateinit var mockView: PlayerSearchPresenter.MVPView

    lateinit var presenter: PlayerSearchPresenter

    val someMatches = (1..5).map {
        Match(id = "uuid00$it", gameMode = "solo-fpp", numberOfKillsForCurrentPlayer = it)
    } as MutableList

    val oneMoreMatch = Match(id = "uuid006", gameMode = "duo-fpp", numberOfKillsForCurrentPlayer = 15)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = createPresenterWithMocks(mockPlayerRepository, mockMatchRepository)
    }

    @Test
    fun `should request player by name on start`() {
        val data = object : PlayerSearchPresenter.InitialData {
            override fun additionalContentsEnabled(): Boolean = true
            override fun getPlayerName(): String = "DiabloVT"
            override fun getRegion(): String = "pc-na"
        }

        runBlocking {
            presenter.initialize()

            presenter.onInitialData(data)
        }

        verify(mockPlayerRepository).getPlayerByName("DiabloVT", "pc-na")
    }

    @Test
    fun `should request player from current region if no region is passed in InitialData`() {
        val data = object : PlayerSearchPresenter.InitialData {
            override fun additionalContentsEnabled(): Boolean = true
            override fun getPlayerName(): String = "Nobunaga"
            override fun getRegion(): String = ""
        }

        givenTheStoredPlayerRegionIs("pc-jp")

        runBlocking {
            presenter.initialize()

            presenter.onInitialData(data)
        }

        verify(mockPlayerRepository).getPlayerByName("Nobunaga", "pc-jp")
    }

    @Test
    fun `should load matches 1 to 5 when search button is clicked`() {
        val data = object : PlayerSearchPresenter.InitialData {
            override fun additionalContentsEnabled(): Boolean = true
            override fun getPlayerName(): String = "DiabloVT"
            override fun getRegion(): String = "pc-na"
        }

        whenever(mockPlayerRepository.getPlayerByName(anyString(), anyString())).thenReturn(
                Either.right(Player(
                        name = "DiabloVT",
                        matches = someMatches
                ))
        )

        runBlocking {
            presenter.initialize()

            presenter.onInitialData(data)
        }

        verify(mockMatchRepository).getMatchById("uuid001")
        verify(mockMatchRepository).getMatchById("uuid002")
        verify(mockMatchRepository).getMatchById("uuid003")
        verify(mockMatchRepository).getMatchById("uuid004")
        verify(mockMatchRepository).getMatchById("uuid005")
    }

    @Test
    fun `should hide "loading" when matches are loaded`() {
        val data = object : PlayerSearchPresenter.InitialData {
            override fun additionalContentsEnabled(): Boolean = true
            override fun getPlayerName(): String = "DiabloVT"
            override fun getRegion(): String = "pc-na"
        }

        whenever(mockPlayerRepository.getPlayerByName(anyString(), anyString())).thenReturn(
                Either.right(Player(
                        name = "DiabloVT",
                        matches = someMatches
                ))
        )

        runBlocking {
            presenter.initialize()

            presenter.onInitialData(data)
        }

        verify(mockView).hideLoading()
    }

    @Test
    fun `should show a "load more" icon if there are more matches`() {
        val data = object : PlayerSearchPresenter.InitialData {
            override fun additionalContentsEnabled(): Boolean = true
            override fun getPlayerName(): String = "DiabloVT"
            override fun getRegion(): String = "pc-na"
        }

        whenever(mockPlayerRepository.getPlayerByName(anyString(), anyString())).thenReturn(
                Either.right(Player(
                        name = "DiabloVT",
                        matches = someMatches.apply { add(oneMoreMatch) }
                ))
        )

        runBlocking {
            presenter.initialize()

            presenter.onInitialData(data)
        }

        verify(mockView).addLoadMoreItem()
    }

    @Test
    fun `should not show a "load more" icon if there are no more matches`() {
        val data = object : PlayerSearchPresenter.InitialData {
            override fun additionalContentsEnabled(): Boolean = true
            override fun getPlayerName(): String = "DiabloVT"
            override fun getRegion(): String = "pc-na"
        }

        whenever(mockPlayerRepository.getPlayerByName(anyString(), anyString())).thenReturn(
                Either.right(Player(
                        name = "DiabloVT",
                        matches = someMatches
                ))
        )

        runBlocking {
            presenter.initialize()

            presenter.onInitialData(data)
        }

        verify(mockView, never()).addLoadMoreItem()
    }

    @Test
    fun `should show "load more" only once if there are ten matches`() {
        val data = object : PlayerSearchPresenter.InitialData {
            override fun additionalContentsEnabled(): Boolean = true
            override fun getPlayerName(): String = "DiabloVT"
            override fun getRegion(): String = "pc-na"
        }

        whenever(mockPlayerRepository.getPlayerByName(anyString(), anyString())).thenReturn(
                Either.right(Player(
                        name = "DiabloVT",
                        matches = (1..10).map {
                            Match(id = "id00$it", gameMode = "solo", numberOfKillsForCurrentPlayer = it)
                        }
                ))
        )

        runBlocking {
            presenter.initialize()

            presenter.onInitialData(data)

            presenter.onLoadMoreMatchesClicked()
        }

        verify(mockView, times(1)).addLoadMoreItem()
    }

    @Test
    fun `should show "content available" button if there is content available for current player`() {
        val data = givenThatInitialDataIsEmpty()

        givenThereIsContentAvailableForAllPlayers()
        givenThatQueryingForAnyPlayerReturns(Player(
                name = "DiabloVT",
                matches = someMatches
        ))

        runBlocking {
            presenter.initialize()

            presenter.onInitialData(data)

            presenter.onSendButtonClicked("DiabloVT")
        }

        verify(mockView).showContentAvailableButton()
    }

    @Test
    fun `should not show "content available" button if this feature is disabled by extras`() {
        val data = object : PlayerSearchPresenter.InitialData {
            override fun additionalContentsEnabled(): Boolean = false
            override fun getPlayerName(): String = "DiabloVT"
            override fun getRegion(): String = "pc-na"
        }

        givenThereIsContentAvailableForAllPlayers()
        givenThatQueryingForAnyPlayerReturns(Player(
                name = "DiabloVT",
                matches = someMatches
        ))

        runBlocking {
            presenter.initialize()

            presenter.onInitialData(data)

            presenter.onSendButtonClicked("DiabloVT")
        }

        verify(mockView, never()).showContentAvailableButton()
    }

    @Test
    fun `should hide "content available" button when a new request is performed`() {
        val data = givenThatInitialDataIsEmpty()

        givenThereIsContentAvailableForAllPlayers()
        givenThatQueryingForAnyPlayerReturns(Player(
                name = "DiabloVT",
                matches = someMatches
        ))

        runBlocking {
            presenter.initialize()

            presenter.onInitialData(data)

            presenter.onSendButtonClicked("DiabloVT")
        }

        verify(mockView).hideContentAvailableButton()
    }

    @Test
    fun `should not show content available button if there is no content available for player`() {
        val data = givenThatInitialDataIsEmpty()

        givenThereIsNoContentAvailableForAnyPlayer()
        givenThatQueryingForAnyPlayerReturns(Player(
                name = "DiabloVT",
                matches = someMatches
        ))

        runBlocking {
            presenter.initialize()

            presenter.onInitialData(data)

            presenter.onSendButtonClicked("DiabloVT")
        }

        verify(mockView, never()).showContentAvailableButton()
    }

    @Test
    fun `should query "is content available" once and only once when a request is performed`() {
        val data = givenThatInitialDataIsEmpty()

        givenThereIsNoContentAvailableForAnyPlayer()
        givenThatQueryingForAnyPlayerReturns(Player(
                name = "DiabloVT",
                matches = someMatches
        ))

        runBlocking {
            presenter.initialize()

            presenter.onInitialData(data)

            presenter.onSendButtonClicked("DiabloVT")
        }

        verify(mockIsContentAvailableForPlayer).isContentAvailableForPlayer(anyPlayer())
    }

    @Test
    fun `should not show content available button if there are no matches for player`() {
        val data = givenThatInitialDataIsEmpty()

        givenThereIsContentAvailableForAllPlayers()
        givenThatQueryingForAnyPlayerReturns(Player(
                name = "DiabloVT",
                matches = emptyList()
        ))

        runBlocking {
            presenter.initialize()

            presenter.onInitialData(data)

            presenter.onSendButtonClicked("DiabloVT")
        }

        verify(mockView, never()).showContentAvailableButton()
    }

    @Test
    fun `should show "empty case" when player has no matches`() {
        val data = object : PlayerSearchPresenter.InitialData {
            override fun additionalContentsEnabled(): Boolean = true
            override fun getPlayerName(): String = "DiabloVT"
            override fun getRegion(): String = "pc-na"
        }

        whenever(mockPlayerRepository.getPlayerByName(anyString(), anyString())).thenReturn(
                Either.right(Player(
                        name = "DiabloVT",
                        matches = emptyList()
                ))
        )

        runBlocking {
            presenter.initialize()

            presenter.onInitialData(data)
        }

        verify(mockView).showEmptyCase()
    }

    @Test
    fun `should hide "empty case" when player has one or more matches`() {
        val data = object : PlayerSearchPresenter.InitialData {
            override fun additionalContentsEnabled(): Boolean = true
            override fun getPlayerName(): String = "DiabloVT"
            override fun getRegion(): String = "pc-na"
        }

        whenever(mockPlayerRepository.getPlayerByName(anyString(), anyString())).thenReturn(
                Either.right(Player(
                        name = "DiabloVT",
                        matches = someMatches
                ))
        )

        runBlocking {
            presenter.initialize()

            presenter.onInitialData(data)
        }

        verify(mockView).hideEmptyCase()
    }

    @Test
    fun `should hide "loading" when player has no matches`() {
        val data = object : PlayerSearchPresenter.InitialData {
            override fun additionalContentsEnabled(): Boolean = true
            override fun getPlayerName(): String = "DiabloVT"
            override fun getRegion(): String = "pc-na"
        }

        whenever(mockPlayerRepository.getPlayerByName(anyString(), anyString())).thenReturn(
                Either.right(Player(
                        name = "DiabloVT",
                        matches = emptyList()
                ))
        )

        runBlocking {
            presenter.initialize()

            presenter.onInitialData(data)
        }

        verify(mockView).hideLoading()
    }

    private fun givenThatQueryingForAnyPlayerReturns(player: Player) {
        whenever(mockPlayerRepository.getPlayerByName(anyString(), anyString())).thenReturn(
                Either.right(player)
        )
    }

    private fun anyPlayer(): Player = any()

    private fun givenThereIsContentAvailableForAllPlayers() {
        whenever(mockIsContentAvailableForPlayer.isContentAvailableForPlayer(anyPlayer()))
                .thenReturn(Either.right(true))
    }

    private fun givenThereIsNoContentAvailableForAnyPlayer() {
        whenever(mockIsContentAvailableForPlayer.isContentAvailableForPlayer(anyPlayer()))
                .thenReturn(Either.right(false))
    }

    private fun givenTheStoredPlayerRegionIs(region: String) {
        whenever(mockGetPlayerRegion.getPlayerRegion()).thenReturn(Either.right(Region(region)))
    }

    private fun givenThatInitialDataIsEmpty(): PlayerSearchPresenter.InitialData {
        return object : PlayerSearchPresenter.InitialData {
            override fun additionalContentsEnabled(): Boolean = true
            override fun getPlayerName(): String = ""
            override fun getRegion(): String = ""
        }
    }

    private fun createPresenterWithMocks(playerRepository: PlayerRepository, matchRepository: MatchRepository): PlayerSearchPresenter {
        val presenter = PlayerSearchPresenter(mockResLocator,
                playerRepository,
                matchRepository,
                mockGetPlayerAccount,
                mockGetCurrentSeason,
                mockGetPlayerSeasonInfo,
                mockIsContentAvailableForPlayer,
                mockGetPlayerRegion)
        presenter.view = mockView
        presenter.navigator = mockNavigator
        return presenter
    }
}
