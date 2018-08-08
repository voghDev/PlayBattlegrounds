package es.voghdev.playbattlegrounds.features.players

import arrow.core.Either
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import es.voghdev.playbattlegrounds.common.AbsError
import es.voghdev.playbattlegrounds.features.players.model.Content
import es.voghdev.playbattlegrounds.features.players.model.Player
import es.voghdev.playbattlegrounds.features.players.usecase.GetContentById
import es.voghdev.playbattlegrounds.features.players.usecase.GetPlayerById
import es.voghdev.playbattlegrounds.features.players.usecase.GetPlayerByName
import es.voghdev.playbattlegrounds.features.players.usecase.InsertContent
import es.voghdev.playbattlegrounds.features.season.Season
import es.voghdev.playbattlegrounds.features.season.model.PlayerSeasonGameModeStats
import es.voghdev.playbattlegrounds.features.season.model.PlayerSeasonInfo
import es.voghdev.playbattlegrounds.features.season.usecase.GetPlayerSeasonInfo
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PlayerRepositoryTest {

    @Mock
    lateinit var mockGetPlayerById: GetPlayerById

    @Mock
    lateinit var mockGetPlayerByName: GetPlayerByName

    @Mock
    lateinit var mockGetPlayerSeasonInfo: GetPlayerSeasonInfo

    @Mock
    lateinit var mockGetContentById: GetContentById

    @Mock
    lateinit var mockGetContentByIdDB: GetContentById

    @Mock
    lateinit var mockInsertContentDB: InsertContent

    lateinit var playerRepository: PlayerRepository

    val aContent = Content(
        id = 1L,
        title = "Example content",
        text = "This is an example content"
    )

    val anotherContent = Content(
        id = 1L,
        title = "Another content",
        text = "This is an example content #2"
    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        playerRepository = createRepositoryWithMocks()
    }

    @Test
    fun `should not throttle anymore if requested six times in less than five minutes`() {
        playerRepository.userCanRequest(1)
        playerRepository.userCanRequest(2)
        playerRepository.userCanRequest(3)
        playerRepository.userCanRequest(4)
        playerRepository.userCanRequest(5)

        assertTrue(playerRepository.userCanRequest(6))
    }

    @Test
    fun `should not throttle the first five requests`() {
        var result = true

        result = result && playerRepository.userCanRequest(1)
        result = result && playerRepository.userCanRequest(2)
        result = result && playerRepository.userCanRequest(3)
        result = result && playerRepository.userCanRequest(4)
        result = result && playerRepository.userCanRequest(5)

        assertTrue(result)
    }

    @Test
    fun `should not throttle six requests spaced five minutes`() {
        playerRepository.userCanRequest(1)
        playerRepository.userCanRequest(2)
        playerRepository.userCanRequest(3)
        playerRepository.userCanRequest(4)
        playerRepository.userCanRequest(5)

        assertTrue(playerRepository.userCanRequest(60006))
    }

    @Test
    fun `should send a different SeasonInfo stats for two players in the same season and instant`() {
        val season = Season("division.bro.official.2018-08", true, false)
        val player1 = Player(name = "eqs_insanity")
        val player2 = Player(name = "DrDisRespect")

        givenThatPlayerSeasonInfoIs(seasonInfoWithKills(100))

        val info1 = playerRepository.getPlayerSeasonInfo(player1, season, 1001)

        givenThatPlayerSeasonInfoIs(seasonInfoWithKills(200))

        val info2 = playerRepository.getPlayerSeasonInfo(player2, season, 1002)

        assertNotEquals((info1 as? Either.Right)?.b?.statsSolo?.kills, (info2 as? Either.Right)?.b?.statsSolo?.kills)
    }

    @Test
    fun `should call the Api only once when three requests are done inside cache TTL time`() {
        givenThatPlayerSeasonInfoIs(seasonInfoWithKills(100))

        val season = Season("division.bro.official.2018-08", true, false)
        val player1 = Player(name = "eqs_insanity")

        playerRepository.getPlayerSeasonInfo(player1, season, 1000)
        playerRepository.getPlayerSeasonInfo(player1, season, 1001)
        playerRepository.getPlayerSeasonInfo(player1, season, 11000)

        verify(mockGetPlayerSeasonInfo, times(1)).getPlayerSeasonInfo(any(), any())
    }

    @Test
    fun `should call the Api twice if two requests are done spaced 10 seconds in time`() {
        givenThatPlayerSeasonInfoIs(seasonInfoWithKills(100))

        val season = Season("division.bro.official.2018-08", true, false)
        val player1 = Player(name = "eqs_insanity")

        playerRepository.getPlayerSeasonInfo(player1, season, 1000)
        playerRepository.getPlayerSeasonInfo(player1, season, 1000 + 10001)

        verify(mockGetPlayerSeasonInfo, times(2)).getPlayerSeasonInfo(any(), any())
    }

    @Test
    fun `should not request a content twice to the Api if it has been cached`() {
        givenThatGettingAContentByIdReturns(aContent)

        playerRepository.getContentById(1L)
        playerRepository.getContentById(1L)

        verify(mockGetContentById, times(1)).getContentById(anyLong())
    }

    @Test
    fun `should not request a content twice to the DB if it has been cached`() {
        givenThatGettingAContentFromDatabaseReturns(aContent)

        playerRepository.getContentById(1L)
        playerRepository.getContentById(1L)

        verify(mockGetContentByIdDB, times(1)).getContentById(anyLong())
    }

    @Test
    fun `should not request the default DataSource if DB already contains a result`() {
        givenThatGettingAContentFromDatabaseReturns(aContent)
        givenThatGettingAContentByIdReturns(anotherContent)

        playerRepository.getContentById(1L)
        playerRepository.getContentById(1L)

        verify(mockGetContentById, never()).getContentById(anyLong())
    }

    @Test
    fun `should request the default DataSource if DB returns an error`() {
        givenThatGettingAContentFromDatabaseReturns(AbsError("File not found"))
        givenThatGettingAContentByIdReturns(anotherContent)

        playerRepository.getContentById(5L)

        verify(mockGetContentById).getContentById(5L)
    }

    private fun givenThatGettingAContentByIdReturns(c: Content) {
        whenever(mockGetContentById.getContentById(anyLong())).thenReturn(Either.right(c))
    }

    private fun givenThatGettingAContentFromDatabaseReturns(anError: AbsError) {
        whenever(mockGetContentByIdDB.getContentById(anyLong())).thenReturn(Either.left(anError))
    }

    private fun givenThatGettingAContentFromDatabaseReturns(content: Content) {
        whenever(mockGetContentByIdDB.getContentById(anyLong())).thenReturn(Either.right(content))
    }

    private fun givenThatPlayerSeasonInfoIs(s: PlayerSeasonInfo) {
        whenever(mockGetPlayerSeasonInfo.getPlayerSeasonInfo(any(), any())).thenReturn(Either.right(s))
    }

    private fun seasonInfoWithKills(kills: Int): PlayerSeasonInfo =
        PlayerSeasonInfo(
            PlayerSeasonGameModeStats(kills = kills, losses = kills / 4, top10s = kills / 2),
            PlayerSeasonGameModeStats(kills = kills, losses = kills / 4, top10s = kills / 2),
            PlayerSeasonGameModeStats(kills = kills, losses = kills / 4, top10s = kills / 2),
            PlayerSeasonGameModeStats(kills = kills, losses = kills / 4, top10s = kills / 2),
            PlayerSeasonGameModeStats(kills = kills, losses = kills / 4, top10s = kills / 2),
            PlayerSeasonGameModeStats(kills = kills, losses = kills / 4, top10s = kills / 2)
        )

    private fun createRepositoryWithMocks(): PlayerRepository {
        return PlayerRepository(
            mockGetPlayerById,
            mockGetPlayerByName,
            "Too many requests",
            mockGetPlayerSeasonInfo,
            mockGetContentById,
            mockGetContentByIdDB,
            mockInsertContentDB
        )
    }
}
