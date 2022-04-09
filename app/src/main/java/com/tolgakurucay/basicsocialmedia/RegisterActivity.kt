package com.tolgakurucay.basicsocialmedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        buttonLogin.setOnClickListener {
            it?.let {

                startActivity(Intent(this,LoginActivity::class.java))

            }
        }




    }

}