package com.music.bitchord.playback

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * The standby is armed a full lead before the transition, so its readiness
 * grace has to start at the transition. Counting it from the arm start dropped
 * a cold stream with most of the lead still to run and produced the
 * `crossfade: skipped ... reason=arm-timeout` hard cut on 11-03.
 */
class CrossfadeArmDeadlineTest {

    @Test
    fun `the arm deadline includes the lead the standby was given`() {
        assertEquals(
            19_000L,
            CrossfadeArmPolicy.armDeadlineMs(startedAtMs = 1_000L, leadMs = 15_000L),
        )
    }

    @Test
    fun `the arm deadline still adds the tempo ramp`() {
        assertEquals(
            20_500L,
            CrossfadeArmPolicy.armDeadlineMs(startedAtMs = 1_000L, leadMs = 15_000L, rampMs = 1_500L),
        )
    }

    @Test
    fun `a negative ramp does not shorten the deadline`() {
        assertEquals(
            19_000L,
            CrossfadeArmPolicy.armDeadlineMs(startedAtMs = 1_000L, leadMs = 15_000L, rampMs = -1_000L),
        )
    }

    @Test
    fun `a negative lead never shortens the grace`() {
        assertEquals(
            4_000L,
            CrossfadeArmPolicy.armDeadlineMs(startedAtMs = 1_000L, leadMs = -5_000L),
        )
    }
}
