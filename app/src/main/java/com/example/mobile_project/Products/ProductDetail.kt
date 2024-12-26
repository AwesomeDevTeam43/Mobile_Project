package com.example.mobile_project.Products

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.mobile_project.R
import com.example.mobile_project.ui.theme.Mobile_ProjectTheme
import java.util.Date

@Composable
fun ProductDetail(modifier: Modifier, url: String) {
    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(true)
            }
        },
            update = { webView ->
                webView.loadUrl(url)
            })
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProductDetail(modifier = Modifier, url = "https://www.google.com")
}