package com.example.exoplayerpoc.player

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.drm.ExoMediaDrm
import androidx.media3.exoplayer.drm.MediaDrmCallback
import com.android.volley.Request.Method
import com.example.exoplayerpoc.http.executePost
import com.example.exoplayerpoc.http.widewineRequest
import com.example.exoplayerpoc.utils.enrichLicenseUrl
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.*

@UnstableApi
class CustomDrmCallback(
    val headers : MutableMap<String, String>,
    private val bodyParams: MutableMap<String, String>,
    val context: Context
) : MediaDrmCallback {
    override fun executeProvisionRequest(
        uuid: UUID,
        request: ExoMediaDrm.ProvisionRequest
    ): ByteArray {
        val url = "${request.defaultUrl}&signedRequest=${String(request.data)}"

        return executePost(
            dataSourceFactory = DefaultHttpDataSource.Factory(),
            url = url,
            httpBody = null,
            requestProperties = mapOf()
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun executeKeyRequest(uuid: UUID, request: ExoMediaDrm.KeyRequest): ByteArray {

        val enrichedUrl = enrichLicenseUrl(headers)

        val challenge = request.data
        val encoded: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(challenge)
        } else {
            String(
                android.util.Base64.encode(challenge, android.util.Base64.DEFAULT),
                StandardCharsets.UTF_8
            )
        }
        bodyParams["widevineChallenge"] = encoded

        val widewineResponse = widewineRequest(
            url = enrichedUrl,
            method = Method.POST,
            body = bodyParams,
            context = context
        )

        Log.e("SBALLO", widewineResponse)

        var license = ""
        try {
            val response = JSONObject(widewineResponse)
            val licenseObject = response.getJSONObject("getWidevineLicenseResponse")
            license = licenseObject.getString("license")
        } catch (t: Throwable) {
            Log.e("ERROR SERIALIZING DRM", t.message.toString())
        }

        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getDecoder().decode(license)
        } else {
            android.util.Base64.decode(license, android.util.Base64.DEFAULT)
        }

        return result
    }
}