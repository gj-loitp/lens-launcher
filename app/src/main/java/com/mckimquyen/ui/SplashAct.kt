package com.mckimquyen.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.mckimquyen.R

class SplashAct : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.a_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ActSettings::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }, 2000)
    }

}
