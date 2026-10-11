package com.example.ui.screens.lab

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.VirtuLabTheme
import com.example.viewmodel.LabUiEffect
import com.example.viewmodel.LabViewModel
import kotlinx.coroutines.flow.collectLatest

class LabJsBridge(private val onReaction: (String) -> Unit) {
    @JavascriptInterface
    fun onReactionTriggered(reactionType: String) {
        onReaction(reactionType)
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ThreeJsLabWebView(
    viewModel: LabViewModel,
    modifier: Modifier = Modifier
) {
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var isLoaded by remember { mutableStateOf(false) }
    val isDark = VirtuLabTheme.colors.isDark

    // Listen to ViewModel UI effects and post to JS
    LaunchedEffect(webViewRef, isLoaded) {
        val webView = webViewRef ?: return@LaunchedEffect
        if (!isLoaded) return@LaunchedEffect

        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is LabUiEffect.TriggerWeb3DReaction -> {
                    val script = "if (window.triggerReaction) { window.triggerReaction('${effect.animationType}', '${effect.equation}'); }"
                    webView.evaluateJavascript(script, null)
                }
                is LabUiEffect.AddWeb3DChemical -> {
                    val script = "if (window.addChemical) { window.addChemical('${effect.name}', '${effect.symbol}', '${effect.colorHex}'); }"
                    webView.evaluateJavascript(script, null)
                }
                is LabUiEffect.SetWeb3DBurner -> {
                    val script = "if (window.setBurnerState) { window.setBurnerState(${effect.enabled}); }"
                    webView.evaluateJavascript(script, null)
                }
                is LabUiEffect.ResetWeb3D -> {
                    val script = "if (window.resetLab) { window.resetLab(); }"
                    webView.evaluateJavascript(script, null)
                }
                is LabUiEffect.ShowToast -> {
                    // Handled in Compose layer
                }
            }
        }
    }

    // Sync theme mode (Dark / Light) with 3D canvas
    LaunchedEffect(isDark, isLoaded, webViewRef) {
        if (isLoaded && webViewRef != null) {
            webViewRef?.evaluateJavascript("if (window.setTheme) { window.setTheme($isDark); }", null)
        }
    }

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { ctx ->
            WebView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(Color.TRANSPARENT)
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    allowFileAccess = true
                    allowContentAccess = true
                    cacheMode = WebSettings.LOAD_DEFAULT
                    loadWithOverviewMode = true
                    useWideViewPort = true
                }

                addJavascriptInterface(
                    LabJsBridge { _ ->
                        // Javascript callback to Android
                    },
                    "AndroidLab"
                )

                webChromeClient = WebChromeClient()
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        isLoaded = true
                        view?.evaluateJavascript("if (window.setTheme) { window.setTheme($isDark); }", null)
                    }
                }

                loadUrl("file:///android_asset/lab3d/index.html")
                webViewRef = this
            }
        },
        update = { webView ->
            webViewRef = webView
        }
    )

    DisposableEffect(Unit) {
        onDispose {
            webViewRef?.destroy()
            webViewRef = null
        }
    }
}
