package com.example.kyohoe.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.example.kyohoe.SearchListResponse
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewStateWithHTMLData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import kotlinx.serialization.json.Json


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                ShowSomething()
            }
        }
    }
}

// val anconnuri = "https://www.youtube.com/channel/UCIsWNZwrpO_CnlaXO5Oc6bQ"
// val passionworship = "https://www.youtube.com/channel/UCBTZoebaG4rvChzKQ2D80-w"
private val channelId = "UCIsWNZwrpO_CnlaXO5Oc6bQ"
private val myApiKey = "YOUR_API_KEY"

@Composable
fun ShowSomething() {
    var outcome: String? by remember { mutableStateOf(null) }
    var response: SearchListResponse? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            outcome =
                URL("https://youtube.googleapis.com/youtube/v3/search?part=snippet&channelId=$channelId&maxResults=5&order=date&type=video&key=$myApiKey").readText()
        }

        if (outcome != null) {
            val jsonString = outcome.toString()
            val parsedResponse = withContext(Dispatchers.Default) {
                Json.decodeFromString<SearchListResponse>(jsonString)
            }
            response = parsedResponse
        } else {
            // Handle the case when json response is empty
            response = null
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        response?.items?.firstOrNull()?.let { item ->
            val thumbHeight = item.snippet.thumbnails.high.height
            val thumbWidth = item.snippet.thumbnails.high.width

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(thumbWidth.toFloat() / thumbHeight.toFloat())
                ) {
                    ExampleYouTube(
                        video = item.id.videoId
                    )
                }
            }
        }
    }
}

@Composable
fun ExampleYouTube(video: String) {
    val webViewState = rememberWebViewStateWithHTMLData(data = getHTML(video))
    WebView(
        state = webViewState,
        modifier = Modifier.fillMaxSize(),
        onCreated = { it.settings.javaScriptEnabled = true },
        onDispose = { it.settings.javaScriptEnabled = false },
    )
}

fun getHTML(ytId: String): String {
    return """
<iframe class="youtube-player" style="border: 0; width: 100%; height: 95%; padding:0px; margin:0px" id="ytplayer" type="text/html" src="https://www.youtube.com/embed/${ytId}?fs=0" frameborder="0">
</iframe>
"""
}
