package com.example.test.data

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

public suspend fun sendGet(link: String): String {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(link)
        .addHeader("X-GitHub-Api-Version", "2022-11-28")
        .addHeader("accept", "application/vnd.github+json")
        .build()

    try {
        val response = client.newCall(request).execute()
        val result = response.body!!.string()
        return result
    }
    catch (e: IOException) {
        return "";
    }
}