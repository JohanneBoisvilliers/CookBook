package com.example.cookbook.onlineREcipe

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.cookbook.R
import kotlinx.android.synthetic.main.activity_recipe_online.*


class RecipeOnlineActivity : AppCompatActivity() {

    lateinit var recipeUrl:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_online)

        recipeUrl = intent.getStringExtra("url")!!
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(recipeUrl)
    }
}
