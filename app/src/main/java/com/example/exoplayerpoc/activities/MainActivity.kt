package com.example.exoplayerpoc.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.exoplayerpoc.composables.AssetSelectionView
import com.example.exoplayerpoc.ui.theme.ExoplayerPocTheme

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ExoplayerPocTheme {
        AssetSelectionView { _, _, _ ->}
    }
}

const val vodUrl = "https://vod.rte.ie/vod-d/RTE_Test_-_Integration/655/424/IP10000879-01-0014/IP10000879-01-0014-1686931010344.ism/.mpd?available=1683621180&amp;expiry=1835771580&amp;ip=85.39.127.164&amp;token1=18c00bd41937f47b5cf73710ccac0114980ef05d9872a226ea943c62015b82b0&amp;filter=systemBitrate%3C%3D7000000&amp;hls_fmp4=true&amp;test" +
        "Dad=true"
const val vodPid = "kqE3tVB0Ax4u"
const val liveUrl = "https://live.rte.ie/live/a/channel3/channel3.isml/.mpd?dvr_window_length=30&amp;available=1685352600&amp;expiry=1685383200&amp;ip=79.17.228.169&amp;filter=systemBitrate%3C%3D7000000&amp;token1=a9436b20f2b9cc6ccebc25aa1f7ab7703e853bac23962521ae851f7d519d1" +
        "52d"
const val livePid = "viZVXPLZgoev"
const val daiLiveUrl = "https://dai.google.com/linear/dash/event/antwa0EiQm2PoHtx4rBtVw/manifest.mpd?dvr_window_length=30&amp;available=1704364500&amp;expiry=1704393832&amp;ip=34.244.19.2&amp;filter=systemBitrate%3C%3D7000000&amp;token1=43f83d560898f9a0089750ff25a7a5c37755469a321b3922f0c03a54b43f2" +
        "974"
const val daiLivePid = "w0ZpXHH7458V"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExoplayerPocTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    AssetSelectionView { url, mediaPid, daiKey ->
                        val i = Intent(
                            this,
                            PlayerActivity::class.java
                        ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        i.putExtra("VIDEO_URL", url.replace(
                            "&amp;",
                            "&"
                        ))
                        i.putExtra("MEDIA_PID", mediaPid)
                        i.putExtra("DAI_KEY", daiKey)
                        startActivity(i)
                    }
                }
            }
        }
    }
}