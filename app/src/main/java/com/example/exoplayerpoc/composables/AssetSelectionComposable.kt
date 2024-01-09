package com.example.exoplayerpoc.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog


@Composable
fun AssetSelectionView(callback: (url: String, mediaPid: String, daiKey: String) -> Unit) {
    val url = remember { mutableStateOf("") }
    val mediaPid = remember { mutableStateOf("") }
    val daiKey = remember { mutableStateOf("") }
    val missingData = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.background(Color.Black).fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Insert streaming data:",
            fontSize = 16.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(40.dp))
        InputTextField(label = "URL", value = url.value, onValueChange = {
            url.value = it
            Log.e("SBALLO", url.value)
        })
        Spacer(modifier = Modifier.height(40.dp))
        InputTextField(label = "MEDIA PID", value = mediaPid.value, onValueChange = {
            mediaPid.value = it
            Log.e("SBALLO", mediaPid.value)
        })
        Spacer(modifier = Modifier.height(40.dp))
        InputTextField(label = "DAI KEY", value = daiKey.value, onValueChange = {
            daiKey.value = it
            Log.e("SBALLO", daiKey.value)
        })
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            modifier = Modifier.align(CenterHorizontally),
            onClick = {
                if (url.value.isEmpty() || mediaPid.value.isEmpty() || daiKey.value.isEmpty()){
                    missingData.value = true
                } else {
                    callback.invoke(url.value, mediaPid.value, daiKey.value)
                }
            })
        {
            Text(text = "Start playback", color = Color.White)
        }

        if (missingData.value){
            MissingDataDialog {
                missingData.value = false
            }
        }
    }
}

@Composable
fun InputTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.White
        )
    )
}

@Composable
fun MissingDataDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
                .background(Color.Black),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = "Missing data",
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }
    }
}