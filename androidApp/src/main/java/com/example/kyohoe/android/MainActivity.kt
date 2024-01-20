package com.example.kyohoe.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.kyohoe.SearchListResponse
import com.example.kyohoe.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import kotlinx.serialization.json.Json


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    ShowSomething(this)
                }
            }
        }
    }

    fun startVideoActivity(video: YouTubeVideo) {
        // instantiate intent and pass extra parameter from product
        val intent = Intent(this, VideoActivity::class.java)
        intent.putExtra(VideoActivity.KEY_VIDEO_ID, video.videoId)
        startActivity(intent)
    }
}

const val envVariableName = "YOUTUBE_API_KEY"

@Composable
fun ShowSomething(ctext: MainActivity) {
    var outcome: String? by remember { mutableStateOf(null) }
    var response: SearchListResponse? by remember { mutableStateOf(null) }
    val myApiKey: String = System.getenv(envVariableName) ?: "YOUR_API_KEY"
    val channelId = "UCIsWNZwrpO_CnlaXO5Oc6bQ"
    // val anconnuri = "https://www.youtube.com/channel/UCIsWNZwrpO_CnlaXO5Oc6bQ"
    // val passionworship = "https://www.youtube.com/channel/UCBTZoebaG4rvChzKQ2D80-w"

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            outcome =
                URL("https://youtube.googleapis.com/youtube/v3/search?part=snippet&channelId=$channelId&maxResults=5&order=date&type=video&key=$myApiKey").readText()
        }

        response = if (outcome != null) {
            val jsonString = outcome.toString()
            val parsedResponse = withContext(Dispatchers.Default) {
                Json.decodeFromString<SearchListResponse>(jsonString)
            }
            parsedResponse
        } else {
            // TODO: Handle the case when json response is empty
            null
        }
    }

    val videos = response?.items?.toVideos() ?: emptyList()

    VideosGrid(videos = videos) {
        ctext.startVideoActivity(it)
    }
}

fun List<SearchResult>.toVideos(): List<YouTubeVideo> {
    return this.map { YouTubeVideo(it.id.videoId, it.snippet.title, it.snippet.thumbnails.high.url) }
}

@Composable
fun VideosGrid(videos: List<YouTubeVideo>, clickFunc: (YouTubeVideo) -> Unit) {

    LazyColumn(
        modifier = Modifier.fillMaxHeight()
    ) {
        items(
            items = videos,
            itemContent = { video: YouTubeVideo ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.clickable {
                            // start VideoActivity and pass the video details
                            clickFunc(video)
                        }
                    ) {
                        AsyncImage(
                            model = video.thumbnailUrl.imageModel(LocalContext.current),
                            contentDescription = video.title,
                            contentScale = ContentScale.Crop,
                        )
                        Text(
                            text = video.title,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        )
    }
}

fun String.imageModel(context: Context): ImageRequest {
    return ImageRequest.Builder(context)
        .data(this)
        .build()
}
