package com.example.kyohoe.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewStateWithHTMLData

class VideoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val video = YouTubeVideo(
            videoId = intent.getStringExtra(KEY_VIDEO_ID).toString(),
            title = "",
            thumbnailUrl = "",
        )
        setContent { VideoYouTube(video) }
    }

    companion object {
        const val KEY_VIDEO_ID = "video_id"
    }
}

@Composable
fun VideoYouTube(video: YouTubeVideo) {

    val webViewState = rememberWebViewStateWithHTMLData(data = getHTML(video.videoId))

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {

        WebView(
            state = webViewState,
            modifier = Modifier.fillMaxSize(),
            onCreated = { it.settings.javaScriptEnabled = true },
            onDispose = { it.settings.javaScriptEnabled = false },
        )
    }
}

fun getHTML(ytId: String): String {
    return """
<iframe class="youtube-player" style="border: 0; width: 100%; height: 100%; padding:0px; margin:0px" id="ytplayer" type="text/html" src="https://www.youtube.com/embed/${ytId}?fs=0" frameborder="0">
</iframe>
"""
}
