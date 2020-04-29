package es.voghdev.playbattlegrounds.features.players.ui.presenter

import android.os.Build.VERSION_CODES.LOLLIPOP
import arrow.core.Either
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
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
import es.voghdev.playbattlegrounds.features.players.model.Content
import es.voghdev.playbattlegrounds.features.players.model.Player
import es.voghdev.playbattlegrounds.features.players.usecase.IsContentAvailableForPlayer
import es.voghdev.playbattlegrounds.features.season.Season
import es.voghdev.playbattlegrounds.features.season.usecase.GetCurrentSeason
import es.voghdev.playbattlegrounds.features.season.usecase.GetPlayerSeasonInfo
import es.voghdev.playbattlegrounds.features.share.GetImagesPath
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
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
    lateinit var mockGetImagesPath: GetImagesPath

    @Mock
    lateinit var mockView: PlayerSearchPresenter.MVPView

    lateinit var presenter: PlayerSearchPresenter

    val someMatches = (1..5).map {
        Match(id = "uuid00$it", gameMode = "solo-fpp", numberOfKillsForCurrentPlayer = it)
    } as MutableList

    val oneMoreMatch = Match(id = "uuid006", gameMode = "duo-fpp", numberOfKillsForCurrentPlayer = 15)

    val contentCaptor = argumentCaptor<Content>()
    val matchesCaptor = argumentCaptor<List<Match>>()

    val aSeason = Season("2020-01", true, false)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = createPresenterWithMocks(mockPlayerRepository, mockMatchRepository)

        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @Test
    fun `should request a player by name to the API on start`() = runBlockingTest {
        val data = object : PlayerSearchPresenter.InitialData {
            override fun additionalContentsEnabled(): Boolean = true
            override fun getPlayerName(): String = "DiabloVT"
            override fun getRegion(): String = "pc-na"
        }

        presenter.initialize()

        presenter.onInitialData(data)

        verify(mockPlayerRepository).getPlayerByName("DiabloVT", "pc-na")
    }

    @Test
    fun `should request player season info on start`() = runBlockingTest {
        val data = object : PlayerSearchPresenter.InitialData {
            override fun additionalContentsEnabled(): Boolean = false
            override fun getPlayerName(): String = "DiabloVT"
            override fun getRegion(): String = "pc-na"
        }

        whenever(mockPlayerRepository.getPlayerByName(anyString(), anyString())).thenReturn(
            Either.right(Player(
                name = "DiabloVT",
                matches = someMatches
            ))
        )

        whenever(mockGetCurrentSeason.getCurrentSeason()).thenReturn(Either.Right(aSeason))

        presenter.initialize()

        presenter.onInitialData(data)

        verify(mockPlayerRepository).getPlayerSeasonInfo(anyPlayer(), anySeason(), anyLong())
    }

    @Test
    fun `should request a player from current region if no region is passed in InitialData`() = runBlockingTest {
        val data = object : PlayerSearchPresenter.InitialData {
            override fun additionalContentsEnabled(): Boolean = true
            override fun getPlayerName(): String = "Nobunaga"
            override fun getRegion(): String = ""
        }

        givenTheStoredPlayerRegionIs("pc-jp")

        presenter.initialize()

        presenter.onInitialData(data)

        verify(mockPlayerRepository).getPlayerByName("Nobunaga", "pc-jp")
    }

    @Test
    fun `should load matches 1 to 5 when search button is clicked`() = runBlockingTest {
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

        presenter.initialize()

        presenter.onInitialData(data)

        verify(mockMatchRepository).getMatchById("uuid001")
        verify(mockMatchRepository).getMatchById("uuid002")
        verify(mockMatchRepository).getMatchById("uuid003")
        verify(mockMatchRepository).getMatchById("uuid004")
        verify(mockMatchRepository).getMatchById("uuid005")
    }

    @Test
    fun `should hide "loading" when matches are loaded`() = runBlockingTest {
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

        presenter.initialize()

        presenter.onInitialData(data)

        verify(mockView).hideLoading()
    }

    @Test
    fun `should show a "load more" icon if there are more matches to load`() = runBlockingTest {
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

        presenter.initialize()

        presenter.onInitialData(data)

        verify(mockView).addLoadMoreItem()
    }

    @Test
    fun `should render View state when some matches are available`() = runBlockingTest {
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

        givenThereAreSomeMatchesForPlayer()

        presenter.initialize()

        presenter.onInitialData(data)

        verify(mockView).addMatches(matchesCaptor.capture())

        assertEquals(5, matchesCaptor.firstValue.size)
    }

    @Test
    fun `should not show a "load more" icon if there are no more matches to load`() = runBlockingTest {
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

        presenter.initialize()

        presenter.onInitialData(data)

        verify(mockView, never()).addLoadMoreItem()
    }

    @Test
    fun `should show "load more" only once if there are exactly ten matches`() = runBlockingTest {
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

        presenter.initialize()

        presenter.onInitialData(data)

        presenter.onLoadMoreMatchesClicked()

        verify(mockView, times(1)).addLoadMoreItem()
    }

    @Test
    fun `should show "content available" button if there is content available for current player`() = runBlockingTest {
        val data = givenThatInitialDataIsEmpty()

        givenThereIsContentAvailableForAllPlayers()
        givenThatQueryingForAnyPlayerReturns(Player(
            name = "DiabloVT",
            matches = someMatches
        ))

        presenter.initialize()

        presenter.onInitialData(data)

        presenter.onSendButtonClicked("DiabloVT")

        verify(mockView).showContentAvailableButton()
    }

    @Test
    fun `should not show "content available" button if this feature is disabled by InitialData`() = runBlockingTest {
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

        presenter.initialize()

        presenter.onInitialData(data)

        presenter.onSendButtonClicked("DiabloVT")

        verify(mockView, never()).showContentAvailableButton()
    }

    @Test
    fun `should hide "content available" button when a new request is performed`() = runBlockingTest {
        val data = givenThatInitialDataIsEmpty()

        givenThereIsContentAvailableForAllPlayers()
        givenThatQueryingForAnyPlayerReturns(Player(
            name = "DiabloVT",
            matches = someMatches
        ))

        presenter.initialize()

        presenter.onInitialData(data)

        presenter.onSendButtonClicked("DiabloVT")

        verify(mockView).hideContentAvailableButton()
    }

    @Test
    fun `should not show "content available" button if there is no content available for current player`() = runBlockingTest {
        val data = givenThatInitialDataIsEmpty()

        givenThereIsNoContentAvailableForAnyPlayer()
        givenThatQueryingForAnyPlayerReturns(Player(
            name = "DiabloVT",
            matches = someMatches
        ))

        presenter.initialize()

        presenter.onInitialData(data)

        presenter.onSendButtonClicked("DiabloVT")

        verify(mockView, never()).showContentAvailableButton()
    }

    @Test
    fun `should query "is content available" once and only once when a request is performed`() = runBlockingTest {
        val data = givenThatInitialDataIsEmpty()

        givenThereIsNoContentAvailableForAnyPlayer()
        givenThatQueryingForAnyPlayerReturns(Player(
            name = "DiabloVT",
            matches = someMatches
        ))

        presenter.initialize()

        presenter.onInitialData(data)

        presenter.onSendButtonClicked("DiabloVT")

        verify(mockIsContentAvailableForPlayer).isContentAvailableForPlayer(anyPlayer())
    }

    @Test
    fun `should not show "content available" button if there are no matches for player`() = runBlockingTest {
        val data = givenThatInitialDataIsEmpty()

        givenThereIsContentAvailableForAllPlayers()
        givenThatQueryingForAnyPlayerReturns(Player(
            name = "DiabloVT",
            matches = emptyList()
        ))

        presenter.initialize()

        presenter.onInitialData(data)

        presenter.onSendButtonClicked("DiabloVT")

        verify(mockView, never()).showContentAvailableButton()
    }

    @Test
    fun `should show "empty case" when player has no matches`() = runBlockingTest {
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

        presenter.initialize()

        presenter.onInitialData(data)

        verify(mockView).showEmptyCase()
    }

    @Test
    fun `should hide "empty case" when player has one or more matches`() = runBlockingTest {
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

        presenter.initialize()

        presenter.onInitialData(data)

        verify(mockView).hideEmptyCase()
    }

    @Test
    fun `should hide "loading" when player has no matches`() = runBlockingTest {
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

        presenter.initialize()

        presenter.onInitialData(data)

        verify(mockView).hideLoading()
    }

    @Test
    fun `should show "share" button once player matches have been loaded`() = runBlockingTest {
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

        presenter.initialize()

        presenter.onInitialData(data)

        verify(mockView).showShareButton()
    }

    @Test
    fun `should hide "share" button if player has no matches`() = runBlockingTest {
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

        presenter.initialize()

        presenter.onInitialData(data)

        verify(mockView).hideShareButton()
    }

    @Test
    fun `should not show "share" button if player has no matches`() = runBlockingTest {
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

        presenter.initialize()

        presenter.onInitialData(data)

        verify(mockView, never()).showShareButton()
    }

    @Test
    fun `should load a sponsored content for player ByRubi9`() = runBlockingTest {
        val data = object : PlayerSearchPresenter.InitialData {
            override fun additionalContentsEnabled(): Boolean = true
            override fun getPlayerName(): String = "ByRubi9"
            override fun getRegion(): String = "pc-eu"
        }

        whenever(mockPlayerRepository.getPlayerByName(anyString(), anyString())).thenReturn(
            Either.right(Player(
                name = "ByRubi9",
                matches = emptyList()
            ))
        )

        presenter.initialize()

        presenter.onInitialData(data)

        presenter.onContentButtonClicked()

        verify(mockNavigator).launchContentDetailScreen(contentCaptor.capture())

        assertEquals(9, contentCaptor.firstValue.id)
    }

    private fun givenThatQueryingForAnyPlayerReturns(player: Player) {
        whenever(mockPlayerRepository.getPlayerByName(anyString(), anyString())).thenReturn(
            Either.right(player)
        )
    }

    private fun anyPlayer(): Player = any()
    private fun anySeason(): Season = any()

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

    private fun givenThereAreSomeMatchesForPlayer() {
        (0..someMatches.size.minus(1)).forEach { i ->
            whenever(mockMatchRepository.getMatchById(someMatches[i].id)).thenReturn(Either.Right(someMatches[i]))
        }
    }

    private fun createPresenterWithMocks(playerRepository: PlayerRepository, matchRepository: MatchRepository): PlayerSearchPresenter {
        val presenter = PlayerSearchPresenter(
            Dispatchers.Main,
            mockResLocator,
            playerRepository,
            matchRepository,
            mockGetPlayerAccount,
            mockGetCurrentSeason,
            mockIsContentAvailableForPlayer,
            mockGetPlayerRegion,
            mockGetImagesPath,
            LOLLIPOP)
        presenter.view = mockView
        presenter.navigator = mockNavigator
        return presenter
    }
}
