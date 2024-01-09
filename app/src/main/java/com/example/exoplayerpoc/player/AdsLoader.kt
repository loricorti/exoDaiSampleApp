package com.example.exoplayerpoc.player

import android.content.Context
import android.os.Bundle
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ima.ImaAdsLoader
import androidx.media3.exoplayer.ima.ImaServerSideAdInsertionMediaSource
import androidx.media3.ui.PlayerView

@UnstableApi
class AdsLoader private constructor() {

    companion object {
        val instance: AdsLoader by lazy { AdsLoader() }
    }

    val keyAdsLoaderState = "ads_loader_state"

    var adsLoader: ImaAdsLoader? = null

    var daiAdsLoader: ImaServerSideAdInsertionMediaSource.AdsLoader? = null
    var daiAdsLoaderState: ImaServerSideAdInsertionMediaSource.AdsLoader.State? = null

    fun release() {
        adsLoader?.release()
        daiAdsLoader?.release()
        adsLoader = null
        daiAdsLoader = null
    }

    fun createAdsDAILoader(
        playerView: PlayerView,
        context: Context
    ){
        if (daiAdsLoader == null) {
            val daiAdsLoaderBuilder =
                ImaServerSideAdInsertionMediaSource.AdsLoader.Builder(context, playerView)
            if (daiAdsLoaderState != null) {
                daiAdsLoaderBuilder.setAdsLoaderState(daiAdsLoaderState!!)
            }
            daiAdsLoader = daiAdsLoaderBuilder.build()
        }
    }

    fun setPlayer(player: Player?) {
        if (player != null) {
            daiAdsLoader?.setPlayer(player)
        }
    }

    fun retrieveImaAdsLoaderState(bundle: Bundle) {
        daiAdsLoaderState =
            ImaServerSideAdInsertionMediaSource.AdsLoader.State.CREATOR.fromBundle(bundle)
    }
}