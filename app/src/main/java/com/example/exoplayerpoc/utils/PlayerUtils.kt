package com.example.exoplayerpoc.utils

import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ima.ImaServerSideAdInsertionUriBuilder

fun enrichLicenseUrl(queryParams: Map<String, String>) : String{
    var licenseUrl = "https://widevine.entitlement.eu.theplatform.com/wv/web/ModularDrm"
    val list = queryParams.toList()
    if (list.isNotEmpty()) {
        licenseUrl += "?${list.first().first}=${list.first().second}"
        if (list.size > 1) {
            list.drop(1).forEach { licenseUrl += "&${it.first}=${it.second}" }
        }
    }
    return licenseUrl
}

@UnstableApi
fun buildMediaItem(googleSsaiKey: String): MediaItem {
    val daiLiveUri = ImaServerSideAdInsertionUriBuilder()
        .setAssetKey(googleSsaiKey)
        .setFormat(C.CONTENT_TYPE_DASH)
        .build()
    return MediaItem.fromUri(daiLiveUri)
}