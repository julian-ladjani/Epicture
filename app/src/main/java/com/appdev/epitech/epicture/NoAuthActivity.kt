package com.appdev.epitech.epicture

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_no_auth.*

class NoAuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_auth)

        authButton.setOnClickListener {
            val intent = Intent(this@NoAuthActivity, AuthenticationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
