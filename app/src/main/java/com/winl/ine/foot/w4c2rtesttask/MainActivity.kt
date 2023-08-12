package com.winl.ine.foot.w4c2rtesttask

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.winl.ine.foot.w4c2rtesttask.databinding.ActivityMainBinding

private const val baseUrl = "http://app.zaimforyou.ru/hello"
private const val PREFS_ID = "id"
private const val PREFS_UUID = "uuid"

class MainActivity : AppCompatActivity(), OnSharedPreferenceChangeListener {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!
    private val webView by lazy { this.binding.mainWebView }
    private val pbLoading by lazy { this.binding.pbLoading }
    private val btnLoad by lazy { this.binding.btnLoad }
    private val tvId by lazy { this.binding.tvID }
    private val tvUuid by lazy { this.binding.tvUUID }
    private val prefs by lazy { this.getSharedPreferences("myPrefs", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBtnClickListener()
        loadDataFromPrefs()
    }

    override fun onResume() {
        super.onResume()
        prefs.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        prefs.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun setupBtnClickListener() {
        btnLoad.setOnClickListener {
            hideView(btnLoad)
            showView(pbLoading)
            setupWebView()
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (sharedPreferences == prefs) {
            if (key == PREFS_UUID) {
                sharedPreferences?.getString(key, "")?.let { setTextInTV(tvUuid, it) }
            } else if (key == PREFS_ID) {
                sharedPreferences?.getString(key, "")?.let { setTextInTV(tvId, it) }
            }
        }
    }

    private fun setupWebView() {
        with(webView) {
            loadUrl(baseUrl)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = myWebClient(prefs, btnLoad, pbLoading)
        }
    }

    private fun loadDataFromPrefs() {
        val id = prefs.getString(PREFS_ID, "") ?: ""
        val uuid = prefs.getString(PREFS_UUID, "") ?: ""
        if (id.isNotEmpty()) setTextInTV(tvId, id)
        if (uuid.isNotEmpty()) setTextInTV(tvUuid, uuid)

    }

    private fun setTextInTV(view: TextView, text: String) {
        if (view == tvId) {
            view.text = this.getString(R.string.id, text)
            showView(view)
        } else if (view == tvUuid) {
            view.text = this.getString(R.string.uuid, text)
            showView(view)
        }
    }

    private class myWebClient(
        private val prefs: SharedPreferences,
        private val btnLoad: Button,
        private val progressBar: ProgressBar
    ) : WebViewClient() {

        override fun onPageFinished(view: WebView?, url: String?) {
            view?.visibility = View.VISIBLE
            btnLoad.visibility = View.GONE
            progressBar.visibility = View.GONE
            super.onPageFinished(view, url)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (url!=null && url.contains("?")){
                val regUuid = Regex("""uuid=[\w\W]*""").find(url ?: "", 0)?.value
                val regId = Regex("""\?id=\d*""").find(url ?: "", 0)?.value
                val uuid = regUuid?.substring(5 until regUuid.length)
                val id = regId?.substring(4 until regId.length)
                if (uuid != null) prefs.edit().putString(PREFS_UUID, uuid).apply()
                if (id != null) prefs.edit().putString(PREFS_ID, id).apply()
            }
            return false
        }

    }


    private fun hideView(view: View) {
        view.visibility = View.GONE
    }

    private fun showView(view: View) {
        view.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}