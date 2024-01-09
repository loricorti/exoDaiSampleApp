package com.example.exoplayerpoc.activities

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.util.UnstableApi
import com.example.exoplayerpoc.player.AdsLoader
import com.example.exoplayerpoc.composables.PlayerComposable
import com.example.exoplayerpoc.ui.theme.ExoplayerPocTheme

class PlayerActivity : ComponentActivity() {

    @UnstableApi
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        if (savedInstanceState != null) {
            val adsLoaderStateBundle = savedInstanceState.getBundle(AdsLoader.instance.keyAdsLoaderState)
            if (adsLoaderStateBundle != null) {
                AdsLoader.instance.retrieveImaAdsLoaderState(adsLoaderStateBundle)
            }
        }

        val extras = intent.extras
        setContent {
            ExoplayerPocTheme {
                PlayerComposable(
                    videoUri = extras?.getString("VIDEO_URL") ?: "",
                    mediaPid = extras?.getString("MEDIA_PID") ?: "",
                    daiKey = extras?.getString("DAI_KEY") ?: "",
                    context = this
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("PLAYER", "RESUME")
    }

    override fun onPause() {
        super.onPause()
        Log.e("PLAYER", "PAUSE")
    }

    override fun onStop() {
        super.onStop()
        Log.e("PLAYER", "STOP")
    }

    /*companion object {
        var isInPipMode: Boolean = false
        var contentGuid: String? = null
        var assetType: String? = null
        var callSign: String? = null
        var isOpen: Boolean = false
        var skipPip: Boolean = false
        var watchTime: Long = 0L
        var sessionStartDate: Long = 0L
        var isPaused: Boolean = true
        var bitrateMax: Int = 0
    }*/
}

fun Context.getActivity(): ComponentActivity? = when (this) {
    is AppCompatActivity -> this
    is PlayerActivity -> this
    else -> null
}