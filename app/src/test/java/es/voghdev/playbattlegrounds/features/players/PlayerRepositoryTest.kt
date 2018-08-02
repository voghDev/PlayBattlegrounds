package es.voghdev.playbattlegrounds.features.players

import arrow.core.Either
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import es.voghdev.playbattlegrounds.features.players.model.Player
import es.voghdev.playbattlegrounds.features.players.usecase.GetPlayerById
import es.voghdev.playbattlegrounds.features.players.usecase.GetPlayerByName
import es.voghdev.playbattlegrounds.features.season.Season
import es.voghdev.playbattlegrounds.features.season.model.PlayerSeasonGameModeStats
import es.voghdev.playbattlegrounds.features.season.model.PlayerSeasonInfo
import es.voghdev.playbattlegrounds.features.season.usecase.GetPlayerSeasonInfo
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PlayerRepositoryTest {

    @Mock
    lateinit var mockGetPlayerById: GetPlayerById

    @Mock
    lateinit var mockGetPlayerByName: GetPlayerByName

    @Mock
    lateinit var mockGetPlayerSeasonInfo: GetPlayerSeasonInfo

    lateinit var playerRepository: PlayerRepository

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
        return PlayerRepository(mockGetPlayerById,
                                mockGetPlayerByName,
                                "Too many requests",
                                mockGetPlayerSeasonInfo)
    }
}
