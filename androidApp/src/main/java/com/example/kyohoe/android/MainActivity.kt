package com.example.kyohoe.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.google.accompanist.web.rememberWebViewStateWithHTMLData

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ExampleYouTube()
        }
    }
}

@Composable
fun ExampleYouTube() {
//    val webViewState = rememberWebViewState(url = "https://www.kangkyu.com")
    val webViewState = rememberWebViewStateWithHTMLData(data = getHTML("Crgj2rwVZQk"))
    WebView(
        state = webViewState,
        modifier = Modifier.fillMaxSize(),
        onCreated = { webView ->
            webView.settings.javaScriptEnabled = true
        }
    )
}

fun getHTML(ytId: String): String {
    return """
<iframe class="youtube-player" style="border: 0; width: 100%; height: 95%; padding:0px; margin:0px" id="ytplayer" type="text/html" src="https://www.youtube.com/embed/${ytId}?fs=0" frameborder="0">
</iframe>
"""
}
