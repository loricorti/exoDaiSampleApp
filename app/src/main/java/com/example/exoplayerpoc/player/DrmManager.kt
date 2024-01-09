package com.example.exoplayerpoc.player

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.drm.DefaultDrmSessionManager
import java.net.URLEncoder

@UnstableApi
class DrmManager private constructor() {

    companion object {
        val instance: DrmManager by lazy { DrmManager() }
    }

    private var mediaPid: String = ""

    fun getDrmManager(
        mpxAccount: String,
        context: Context
    ): DefaultDrmSessionManager {
        val headers: MutableMap<String, String> = HashMap()
        headers["form"] = "json"
        headers["schema"] = "1.0"
        headers["token"] = "eyJhbGciOiJSUzUxMiJ9.eyJzdWIiOiJydGUtcHJkLXByZC10cnQvYW5vbnltb3VzX25ncnBfcGxheWVyQHJ0ZS5pZSIsImlzcyI6IjEiLCJleHAiOjE3MDQ0NDQ0NDksImlhdCI6MTcwNDM1ODA0OTQ2MiwianRpIjoiNTlkNmJmYTktNjY4NS00MzYyLTg4MjMtZjMxZWFlOTE3MDI0IiwiZGlkIjoicnRlLXByZC1wcmQtdHJ0IiwidW5tIjoiYW5vbnltb3VzX25ncnBfcGxheWVyQHJ0ZS5pZSIsImN0eCI6IntcInVzZXJOYW1lXCI6XCJhbm9ueW1vdXNfbmdycF9wbGF5ZXJAcnRlLmllXCJ9XG4iLCJvaWQiOiIyNzAwODk0MDAxIn0.bWCMQ-y_fGpH5oMZB6KKzVVqXTsMHD2UciLXn7b6T1QPmOgnTmxe6SZJXf9E5uQR4oEfhwSAN_aHr7Fm1Ul9KsPEVa8-56H8VrWDk6TA7G4s1X4L5C5TqBQ_oxSYcu7IbQvM2ObaPOI3eKUcMqe9a1H71UVYvgddwiZ2h8Fw1t46xmqXhH3LSYoQEkvyV7d0EkQGAnonEjJkkayXzq69i1v5b353MY_GLcvVvGAeMlxtKWht23iHjNiL9ygrUZFTUG0h065J95-ZkHJ0XXd7EUZMiLftrHX1ln_pKjjPv6twWag4h5jZpoomRJb3ZD3gnZH9zrLqX7jMul-k29A" +
                "zXw"
        headers["account"] = URLEncoder.encode(mpxAccount, "utf-8")

        return DefaultDrmSessionManager.Builder()
            .build(
                CustomDrmCallback(
                    headers = headers,
                    bodyParams = mutableMapOf("releasePid" to mediaPid),
                    context = context
                )
            )
    }
}