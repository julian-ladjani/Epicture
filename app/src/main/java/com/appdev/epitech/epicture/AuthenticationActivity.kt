package com.appdev.epitech.epicture

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appdev.epitech.epicture.api.ImgurApi
import kotlinx.android.synthetic.main.activity_authentication.*

class AuthenticationActivity : AppCompatActivity() {
    val OAUTH_LINK = "https://api.imgur.com/oauth2/authorize?client_id=${ImgurApi.clientId}&response_type=token"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        authWebView.settings.javaScriptEnabled = true
        authWebView.webViewClient = WebViewClientAuth(this@AuthenticationActivity, this)
        authWebView.settings.userAgentString = "Epicture"
        authWebView.loadUrl(OAUTH_LINK)
    }
}
