package es.voghdev.playbattlegrounds.features.matches

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MatchTest {
    @Test
    fun `should recognize "pgi" as a Squad GameMode`() {
        val match = Match(gameMode = "pgi")

        assertTrue(match.isDuoOrSquad())
    }

    @Test
    fun `should recognize "pgifpp" as a Squad GameMode`() {
        val match = Match(gameMode = "pgifpp")

        assertTrue(match.isDuoOrSquad())
    }

    @Test
    fun `should recognize "squad" as a Squad GameMode`() {
        val match = Match(gameMode = "squad")

        assertTrue(match.isDuoOrSquad())
    }

    @Test
    fun `should recognize "squad-fpp" as a Squad GameMode`() {
        val match = Match(gameMode = "squad-fpp")

        assertTrue(match.isDuoOrSquad())
    }

    @Test
    fun `should recognize "duo" as a Squad GameMode`() {
        val match = Match(gameMode = "duo")

        assertTrue(match.isDuoOrSquad())
    }

    @Test
    fun `should recognize "duo-fpp" as a Squad GameMode`() {
        val match = Match(gameMode = "duo-fpp")

        assertTrue(match.isDuoOrSquad())
    }

    @Test
    fun `should not recognize "solo" as a Squad GameMode`() {
        val match = Match(gameMode = "solo")

        assertFalse(match.isDuoOrSquad())
    }

    @Test
    fun `should not recognize "solo-fpp" as a Squad GameMode`() {
        val match = Match(gameMode = "solo-fpp")

        assertFalse(match.isDuoOrSquad())
    }
}