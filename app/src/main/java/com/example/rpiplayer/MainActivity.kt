package com.example.rpiplayer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

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
                    val jsonBody = """{"url":"$receivedText"}"""

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

        val button = findViewById<Button>(R.id.myButton)

        button.setOnClickListener {
            sendGetRequest("http://192.168.1.1:7790/shutdown")
        }
    }
}


