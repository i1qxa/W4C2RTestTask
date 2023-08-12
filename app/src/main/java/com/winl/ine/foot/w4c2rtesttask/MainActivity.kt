package com.winl.ine.foot.w4c2rtesttask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.winl.ine.foot.w4c2rtesttask.databinding.ActivityMainBinding

private const val baseUrl = "http://app.zaimforyou.ru/hello"

class MainActivity : AppCompatActivity() {

    private var _binding:ActivityMainBinding?=null
    private val binding:ActivityMainBinding
        get() = _binding!!
    private val webView by lazy { this.binding.mainWebView }
    private val pbLoading by lazy { this.binding.pbLoading }
    private val btnLoad by lazy { this.binding.btnLoad }
    private val tvId by lazy { this.binding.tvID }
    private val tvUuid by lazy { this.binding.tvUUID }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBtnClickListener()
    }

    private fun setupBtnClickListener(){
        btnLoad.setOnClickListener {
                hideView(btnLoad)
                showView(pbLoading)
                loadPage()
        }
    }

    private fun setupWebView(){
        with(webView){
            loadUrl(baseUrl)
            webViewClient = webViewClient()
        }
    }

    private class webViewClient():WebViewClient(){

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            Log.d("URL", url.toString())
            return super.shouldOverrideUrlLoading(view, url)
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            Log.d("URL", request.toString())

            return super.shouldOverrideUrlLoading(view, request)
        }
    }

    private fun loadPage(){
        setupWebView()
    }

    private fun hideView(view: View){
        view.visibility = View.GONE
    }

    private fun showView(view: View){
        view.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}