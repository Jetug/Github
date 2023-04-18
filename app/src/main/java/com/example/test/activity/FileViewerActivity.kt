package com.example.test.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.test.databinding.ActivityFileViewerBinding

private const val URL = "html_url"

fun viewFile(url: String, context: Context) {
    val intent = Intent(context, FileViewerActivity::class.java).apply {
        putExtra(URL, url)
    }
    context.startActivity(intent)
}

class FileViewerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFileViewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.webView.loadUrl(intent.getStringExtra(URL)!!)
    }
}