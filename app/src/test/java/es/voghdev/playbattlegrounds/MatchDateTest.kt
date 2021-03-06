/*
 * Copyright (C) 2018 Olmo Gallegos Hernández.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.voghdev.playbattlegrounds

import org.junit.Assert.assertEquals
import org.junit.Test

class MatchDateTest {
    @Test
    fun `should parse match date in PUBG Api format`() {
        val parsed = "2018-05-01T22:45:35Z".toDate("yyyy-MM-dd")

        assertEquals(1525125600000L, parsed)
    }

    @Test
    fun `should also parse match time supporting PUBG Api format`() {
        val parsed = "2018-05-08T11:04:28Z".toDate("yyyy-MM-dd hh:mm:ss")

        assertEquals(1525770268000L, parsed)
    }
}