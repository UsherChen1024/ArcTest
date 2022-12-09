package com.example.arctest.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.arctest.databinding.ActivityWebBinding


class WebActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebBinding
    private lateinit var url: String

    companion object{
        const val KEY_URL = "web_url"
        fun start(context: Context, url: String){
            context.startActivity(Intent(context, WebActivity::class.java).apply {
                putExtra(KEY_URL, url)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ensureUrl()
        binding.webView.webViewClient = MyWebViewClient()
//        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl(url)
    }

    private fun ensureUrl() {
        url = intent.getStringExtra(KEY_URL) ?: ""
        if (url.isNullOrEmpty()) {
            Toast.makeText(this, "url invalid! please retry again. ", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.webView.canGoBack()) {
            binding.webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            return false
        }
    }
}