package cn.com.hwtc.video.listener

import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.source.MediaSourceEventListener
import com.google.android.exoplayer2.upstream.DataSpec
import java.io.IOException

class PlayerMediaSourceEventListener() : MediaSourceEventListener {
    override fun onLoadStarted(dataSpec: DataSpec?, dataType: Int, trackType: Int, trackFormat: Format?, trackSelectionReason: Int, trackSelectionData: Any?, mediaStartTimeMs: Long, mediaEndTimeMs: Long, elapsedRealtimeMs: Long) {

    }

    override fun onDownstreamFormatChanged(trackType: Int, trackFormat: Format?, trackSelectionReason: Int, trackSelectionData: Any?, mediaTimeMs: Long) {

    }

    override fun onUpstreamDiscarded(trackType: Int, mediaStartTimeMs: Long, mediaEndTimeMs: Long) {

    }

    override fun onLoadCompleted(dataSpec: DataSpec?, dataType: Int, trackType: Int, trackFormat: Format?, trackSelectionReason: Int, trackSelectionData: Any?, mediaStartTimeMs: Long, mediaEndTimeMs: Long, elapsedRealtimeMs: Long, loadDurationMs: Long, bytesLoaded: Long) {

    }

    override fun onLoadCanceled(dataSpec: DataSpec?, dataType: Int, trackType: Int, trackFormat: Format?, trackSelectionReason: Int, trackSelectionData: Any?, mediaStartTimeMs: Long, mediaEndTimeMs: Long, elapsedRealtimeMs: Long, loadDurationMs: Long, bytesLoaded: Long) {

    }

    override fun onLoadError(dataSpec: DataSpec?, dataType: Int, trackType: Int, trackFormat: Format?, trackSelectionReason: Int, trackSelectionData: Any?, mediaStartTimeMs: Long, mediaEndTimeMs: Long, elapsedRealtimeMs: Long, loadDurationMs: Long, bytesLoaded: Long, error: IOException?, wasCanceled: Boolean) {

    }

}