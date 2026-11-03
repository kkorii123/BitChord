package com.music.bitchord.data.net

import com.music.bitchord.data.model.Song
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UnplayableAlternateRecordingTest {

    private fun video(id: String, title: String = "Song", artist: String = "Artist") =
        Song(videoId = id, title = title, artist = artist, thumbnailUrl = null, isVideo = true)

    private fun song(id: String, title: String = "Song", artist: String = "Artist") =
        Song(videoId = id, title = title, artist = artist, thumbnailUrl = null)

    @Test
    fun `another ATV of a region-blocked song is a rescue`() {
        val failed = song("7QihqRWMrz8")
        val other = song("DUQUqfL_4sg")
        assertTrue(UnplayableAlternate.allowsAlternateRecording(failed, downloaded = false))
        assertEquals(
            other,
            UnplayableAlternate.cachedAlternate(
                row = failed,
                cached = other,
                downloaded = false,
                unplayable = false,
            ),
        )
    }

    @Test
    fun `an already-a-song row still keeps its own upload when it must`() {
        val failed = song("7QihqRWMrz8")
        val other = song("DUQUqfL_4sg")
        assertFalse(
            UnplayableAlternate.allowsAlternateRecording(
                failed.copy(videoRadio = true),
                downloaded = false,
            ),
        )
        assertFalse(
            UnplayableAlternate.allowsAlternateRecording(
                failed.copy(title = "Song (Cover)"),
                downloaded = false,
            ),
        )
        assertFalse(UnplayableAlternate.allowsAlternateRecording(failed, downloaded = true))
        assertNull(
            UnplayableAlternate.cachedAlternate(
                row = failed.copy(videoRadio = true),
                cached = other,
                downloaded = false,
                unplayable = false,
            ),
        )
        assertNull(
            UnplayableAlternate.cachedAlternate(
                row = failed,
                cached = other,
                downloaded = true,
                unplayable = false,
            ),
        )
    }

    @Test
    fun `an alternate is never the failed id, a video or a refused copy`() {
        val failed = song("7QihqRWMrz8")
        assertNull(
            UnplayableAlternate.cachedAlternate(
                row = failed,
                cached = song("7QihqRWMrz8"),
                downloaded = false,
                unplayable = false,
            ),
        )
        assertNull(
            UnplayableAlternate.cachedAlternate(
                row = failed,
                cached = video("DUQUqfL_4sg"),
                downloaded = false,
                unplayable = false,
            ),
        )
        assertNull(
            UnplayableAlternate.cachedAlternate(
                row = failed,
                cached = song("DUQUqfL_4sg"),
                downloaded = false,
                unplayable = true,
            ),
        )
    }

    @Test
    fun `song-version eligibility is unchanged for an already-a-song row`() {
        assertFalse(
            UnplayableAlternate.allowsSongVersion(
                song("7QihqRWMrz8"),
                downloaded = false,
            ),
        )
        assertTrue(
            UnplayableAlternate.allowsSongVersion(
                video("7QihqRWMrz8"),
                downloaded = false,
            ),
        )
    }
}
