package com.music.bitchord.data.innertube

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * A bot challenge is about the network, so the optional cover loop must not
 * keep fetching the refused anonymous watch page once per track.
 */
class VideoLoopBotChallengeTest {

    @Test
    fun `a bot challenge stands the video loop down for the refresh window`() {
        StreamResolver.resetBotChallengeForTest()
        try {
            val at = 1_000_000L
            assertFalse(StreamResolver.botChallengeStandDownForTest(at))

            StreamResolver.noteBotChallengeForTest(at)
            assertTrue(StreamResolver.botChallengeStandDownForTest(at))
            assertTrue(StreamResolver.botChallengeStandDownForTest(at + 10 * 60 * 1000L - 1))

            assertFalse(StreamResolver.botChallengeStandDownForTest(at + 10 * 60 * 1000L))
        } finally {
            StreamResolver.resetBotChallengeForTest()
        }
    }
}
