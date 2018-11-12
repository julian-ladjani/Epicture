package com.appdev.epitech.epicture

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appdev.epitech.epicture.api.ImgurApi
import com.github.kittinunf.fuel.core.FuelManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val (isLoggedIn, result) = SecretUtils.getSecrets(this)

        val intent = if (isLoggedIn) {
            FuelManager.instance.basePath = "https://api.imgur.com/3"
            FuelManager.instance.baseHeaders = mapOf("Authorization" to "Bearer ${result.accessToken}")
            Intent(this, GridActivity::class.java)

        } else {
            Intent(this, NoAuthActivity::class.java)
        }

        startActivity(intent)
        finish()
    }

}
