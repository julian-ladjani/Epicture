package com.appdev.epitech.epicture

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appdev.epitech.epicture.api.ImgurApi
import com.appdev.epitech.epicture.api.ImgurApi.Companion.clientId
import com.github.kittinunf.fuel.core.FuelManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val (isLoggedIn, _) = SecretUtils.getSecrets(this)

        val intent = if (isLoggedIn) {
            val prefs = this.getSharedPreferences("secret", Context.MODE_PRIVATE)
            val accessToken = prefs.getString("accessToken", "")
            FuelManager.instance.baseHeaders = mapOf("Authorization" to "Bearer $accessToken", "Authorization" to "Client-ID $clientId")
            //println(FuelManager.instance.baseHeaders)
            FuelManager.instance.basePath = "https://api.imgur.com/3"
            ImgurApi.getGallery(0,0,false)
            Intent(this, GridActivity::class.java)

        } else {
            Intent(this, NoAuthActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}
