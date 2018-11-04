package com.appdev.epitech.epicture

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_no_auth.*

class NoAuthActivity : Activity() {

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
