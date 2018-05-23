package es.voghdev.playbattlegrounds.features.players

import es.voghdev.playbattlegrounds.features.players.usecase.GetPlayerById
import es.voghdev.playbattlegrounds.features.players.usecase.GetPlayerByName
import org.junit.Assert.assertFalse
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

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `should return false if requested six times in less than five minutes`() {
        val repo = PlayerRepository(mockGetPlayerById,
                mockGetPlayerByName,
                "Too many requests")

        repo.userCanRequest(1)
        repo.userCanRequest(2)
        repo.userCanRequest(3)
        repo.userCanRequest(4)
        repo.userCanRequest(5)

        assertFalse(repo.userCanRequest(6))
    }

    @Test
    fun `should not throttle the first five requests`() {
        var result = true
        val repo = PlayerRepository(mockGetPlayerById,
                mockGetPlayerByName,
                "Too many requests")

        result = result && repo.userCanRequest(1)
        result = result && repo.userCanRequest(2)
        result = result && repo.userCanRequest(3)
        result = result && repo.userCanRequest(4)
        result = result && repo.userCanRequest(5)

        assertTrue(result)
    }

    @Test
    fun `should not throttle six requests spaced five minutes`() {
        val repo = PlayerRepository(mockGetPlayerById,
                mockGetPlayerByName,
                "Too many requests")

        repo.userCanRequest(1)
        repo.userCanRequest(2)
        repo.userCanRequest(3)
        repo.userCanRequest(4)
        repo.userCanRequest(5)

        assertFalse(repo.userCanRequest(60006))
    }
}
