package com.example.exoplayerpoc.http

import android.content.Context
import android.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DataSourceInputStream
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.HttpDataSource
import androidx.media3.datasource.StatsDataSource
import androidx.media3.exoplayer.drm.MediaDrmCallbackException
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.nio.charset.Charset
import java.util.concurrent.ExecutionException


fun httpRequest(url : String, method : Int = Request.Method.GET, body : MutableMap<String, String> = mutableMapOf(), context : Context, ok: (response: String) -> Unit, ko: (response: String) -> Unit){
    val req = object : StringRequest(
        method,
        url,
        Response.Listener { response ->
            Log.e("HTTP GET SUCCESS",
                "Response \nbody: $response"
            )
            ok(response)
        },
        Response.ErrorListener { error ->
            error.networkResponse?.let {
                //val statusCode = it.statusCode
                val data = String(it.data)
                Log.e("HTTP GET ERROR",
                    "Response \nstatus code: ${error.networkResponse?.statusCode}" +
                            "\nerror message: ${error.message}" +
                            "\ndata: $data"
                )
            }
            ko(error.message ?: "")
        }) {

        override fun getBodyContentType(): String {
            return "application/json; charset=utf-8"
        }

        override fun getBody(): ByteArray? {
            if (body.isEmpty()) {
                return null
            }
            val jsonBody = JSONObject()
            val params = JSONObject()
            body.forEach{params.put(it.key, it.value)}
            jsonBody.put("getWidevineLicense", params)
            val requestBody = jsonBody.toString()

            Log.e("BODY", "requestBody: $requestBody")

            return requestBody.toByteArray(Charset.defaultCharset())
        }

        override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
            return super.parseNetworkResponse(response)
        }
    }

    Volley.newRequestQueue(context).add(req)
}

fun widewineRequest(url : String, method : Int, body : MutableMap<String, String>, context : Context) : String {
    val future : RequestFuture<String> = RequestFuture.newFuture()
    val req = object : StringRequest(
        /* method = */ method,
        /* url = */ url,
        /* listener = */ future,
        /* errorListener = */ future) {

        override fun getBodyContentType(): String {
            return "application/json; charset=utf-8"
        }

        override fun getBody(): ByteArray? {
            if (body.isEmpty()) {
                return null
            }
            val jsonBody = JSONObject()
            val params = JSONObject()
            body.forEach{params.put(it.key, it.value)}
            jsonBody.put("getWidevineLicense", params)
            val requestBody = jsonBody.toString()

            Log.e("BODY", "requestBody: $requestBody")

            return requestBody.toByteArray(Charset.defaultCharset())
        }

        override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
            return super.parseNetworkResponse(response)
        }
    }

    Volley.newRequestQueue(context).add(req)
    var response = ""

    try {
        response= future.get() // this will block
    } catch (e: InterruptedException) {
        // exception handling
    } catch (e: ExecutionException) {
        // exception handling
    }

    return response
}

@UnstableApi
fun executePost(
    dataSourceFactory: DataSource.Factory,
    url: String,
    httpBody: ByteArray?,
    requestProperties: Map<String, String>
): ByteArray {
    val dataSource = StatsDataSource(dataSourceFactory.createDataSource())
    var manualRedirectCount = 0
    var dataSpec = DataSpec.Builder()
        .setUri(url)
        .setHttpRequestHeaders(requestProperties)
        .setHttpMethod(DataSpec.HTTP_METHOD_POST)
        .setHttpBody(httpBody)
        .setFlags(DataSpec.FLAG_ALLOW_GZIP)
        .build()
    val originalDataSpec = dataSpec
    try {
        while (true) {
            val inputStream = DataSourceInputStream(dataSource, dataSpec)
            dataSpec = try {
                return Util.toByteArray(inputStream)
            } catch (e: HttpDataSource.InvalidResponseCodeException) {
                val redirectUrl: String = getRedirectUrl(e, manualRedirectCount) ?: throw e
                manualRedirectCount++
                dataSpec.buildUpon().setUri(redirectUrl).build()
            } finally {
                Util.closeQuietly(inputStream)
            }
        }
    } catch (e: Exception) {
        throw MediaDrmCallbackException(
            originalDataSpec,
            dataSource.lastOpenedUri,
            dataSource.responseHeaders,
            dataSource.bytesRead,  /* cause= */
            e
        )
    }
}

@UnstableApi
private fun getRedirectUrl(
    exception: HttpDataSource.InvalidResponseCodeException, manualRedirectCount: Int
): String? {
    // For POST requests, the underlying network stack will not normally follow 307 or 308
    // redirects automatically. Do so manually here.
    val manuallyRedirect = ((exception.responseCode == 307 || exception.responseCode == 308)
            && manualRedirectCount < 5)
    if (!manuallyRedirect) {
        return null
    }
    val headerFields = exception.headerFields
    val locationHeaders = headerFields["Location"]
    if (!locationHeaders.isNullOrEmpty()) {
        return locationHeaders[0]
    }
    return null
}