package cn.com.hwtc.video.listener

import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray

class PlayerEventListener : Player.DefaultEventListener() {
    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
        super.onPlaybackParametersChanged(playbackParameters)
    }

    override fun onSeekProcessed() {
        super.onSeekProcessed()
    }

    override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
        super.onTracksChanged(trackGroups, trackSelections)
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        super.onPlayerError(error)
    }

    override fun onLoadingChanged(isLoading: Boolean) {
        super.onLoadingChanged(isLoading)
    }

    override fun onPositionDiscontinuity(reason: Int) {
        super.onPositionDiscontinuity(reason)
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
        super.onRepeatModeChanged(repeatMode)
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
        super.onShuffleModeEnabledChanged(shuffleModeEnabled)
    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
        super.onTimelineChanged(timeline, manifest, reason)
    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {
        super.onTimelineChanged(timeline, manifest)
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        super.onPlayerStateChanged(playWhenReady, playbackState)
    }
}