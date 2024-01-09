package com.example.exoplayerpoc.composables

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.exoplayerpoc.player.AdsLoader
import com.example.exoplayerpoc.activities.PlayerActivity
import com.example.exoplayerpoc.activities.getActivity
import com.example.exoplayerpoc.utils.buildMediaItem
import com.example.exoplayerpoc.player.buildPlayer
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@UnstableApi
@Composable
fun PlayerComposable(videoUri: String, mediaPid: String, daiKey: String, context: Context) {
    val playerView by remember { mutableStateOf(PlayerView(context)) }
    var exoPlayer: ExoPlayer? by remember { mutableStateOf(null) }
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    BackHandler {
        exoPlayer?.release()
        (context.getActivity() as PlayerActivity).finish()
    }

    DisposableEffect(LocalLifecycleOwner.current) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    exoPlayer?.pause()
                }

                Lifecycle.Event.ON_RESUME -> {
                    exoPlayer?.play()
                }

                else -> {
                    Log.e("Lifecycle Event", event.toString())
                }
            }
        }
        val lifecycle = lifecycleOwner.value.lifecycle
        lifecycle.addObserver(observer)

        onDispose {
            exoPlayer?.release()
            lifecycle.removeObserver(observer)
        }
    }

    Log.e("SBALLO", "Video url : $videoUri")
    Log.e("SBALLO", "Media pid : $mediaPid")

    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        factory = {
            AdsLoader.instance.createAdsDAILoader(
                playerView = playerView,
                context = context
            )

            exoPlayer = buildPlayer(context = context)

            playerView.apply {
                exoPlayer?.let { player ->
                    player.videoScalingMode =
                        C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                }
                player = exoPlayer
                AdsLoader.instance.setPlayer(player = player)
            }.also {
                val mediaItem = buildMediaItem(googleSsaiKey = daiKey)
                it.useController = true

                exoPlayer?.setMediaItem(mediaItem)
                exoPlayer?.prepare()

                //AUTOPLAY
                exoPlayer?.let { player -> player.playWhenReady = true }
            }
        }
    )
}