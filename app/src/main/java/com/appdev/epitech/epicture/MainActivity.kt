package com.appdev.epitech.epicture

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val (isLoggedIn, _) = SecretUtils.getSecrets(this)

        val intent = if (isLoggedIn) {
            Intent(this, GridActivity::class.java)

        } else {
            Intent(this, NoAuthActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}
