package com.appdev.epitech.epicture

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
//import com.imgurisnotimgur.background.CommentListener

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val (isLoggedIn, _) = SecretUtils.getSecrets(this)

        val intent = if (isLoggedIn) {
            //CommentListener.initJob(this)
            //Intent(this, SubredditActivity::class.java)
            Intent(this@MainActivity, GridActivity::class.java)

        } else {
            Intent(this@MainActivity, NoAuthActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}
