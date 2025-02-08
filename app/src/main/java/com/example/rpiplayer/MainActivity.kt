package com.example.rpiplayer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

val TAG = "Mainactivity===="


fun sendPostRequest(url: String, jsonBody: String) {
    val client = OkHttpClient()

    // JSON Media Type
    val JSON = "application/json; charset=utf-8".toMediaType()

    // Create JSON body with the parameter 'url'

    // Create the RequestBody
    val requestBody = RequestBody.create(JSON, jsonBody)

    // Build the POST request
    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    // Execute the request in a background thread
    Thread {
        try {
            val response: Response = client.newCall(request).execute()
            if (response.isSuccessful) {
                Log.d(TAG, "Response: ${response.body?.string()}")
            } else {
                Log.d(TAG, "Error: ${response.code}")
            }
        } catch (e: Exception) {
            Log.d(TAG, "Exception: ${e.message}")
        }
    }.start()
}

fun sendGetRequest(url: String) {
    val client = OkHttpClient()

    // Build the POST request
    val request = Request.Builder()
        .url(url)
        .build()

    // Execute the request in a background thread
    Thread {
        try {
            val response: Response = client.newCall(request).execute()
            if (response.isSuccessful) {
                Log.d(TAG, "Response: ${response.body?.string()}")
            } else {
                Log.d(TAG, "Error: ${response.code}")
            }
        } catch (e: Exception) {
            Log.d(TAG, "Exception: ${e.message}")
        }
    }.start()
}

fun filterTextBeforeUrl(input: String): String {
    val urlPattern = Regex("https?://\\S+")
    return urlPattern.find(input)?.value ?: input
}

class MainActivity : ComponentActivity() {


    private fun onSharedIntent() {
        val receivedIntent = intent
        val receivedAction = receivedIntent.action
        val receivedType = receivedIntent.type
        Log.d(TAG, receivedAction + " " + receivedType)
        if (receivedAction == Intent.ACTION_SEND) {
            // check mime type
            if (receivedType!!.startsWith("text/")) {
                val receivedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT)
                Log.d(TAG, receivedText.toString())
                if (receivedText != null) {
                    val filteredText = filterTextBeforeUrl(receivedText)
                    val jsonBody = """{"url":"$filteredText"}"""

                    sendPostRequest(
                        "http://192.168.1.1:7790/process",
                        jsonBody
                    ) // save to your own Uri object
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onSharedIntent()
        enableEdgeToEdge()

        val button = findViewById<ImageButton>(R.id.ShutdownButton)

        button.setOnClickListener {
            sendGetRequest("http://192.168.1.1:7790/shutdown")

        }

        var soundSeekBar = findViewById<SeekBar>(R.id.soundSeekBar)
        val soundLevelTextView = findViewById<TextView>(R.id.soundLevelTextView)

        soundSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                soundLevelTextView.text = "Sound Level: $progress"

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                sendGetRequest("http://192.168.1.1:7790/changesound")
            }

        })
    }


}


