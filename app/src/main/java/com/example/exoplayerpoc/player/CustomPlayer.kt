package com.example.exoplayerpoc.player

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.ima.ImaServerSideAdInsertionMediaSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory

@UnstableApi
fun buildPlayer(context: Context): ExoPlayer {
    val mediaSourceFactory =
        DefaultMediaSourceFactory(context)
            .setDrmSessionManagerProvider {
                DrmManager.instance.getDrmManager(
                    mpxAccount = "http://access.auth.theplatform.com/data/Account/2700894001",
                    context = context
                )
            }

    val adsMediaSourceFactory = ImaServerSideAdInsertionMediaSource.Factory(
        AdsLoader.instance.daiAdsLoader!!,
        mediaSourceFactory
    )
    mediaSourceFactory.setServerSideAdInsertionMediaSourceFactory(adsMediaSourceFactory)

    return ExoPlayer.Builder(context)
        .setMediaSourceFactory(mediaSourceFactory)
        .build()
}

data class VideoQualityInfo(
    val bitrate: Int,
    val width: Int,
    val height: Int
)

