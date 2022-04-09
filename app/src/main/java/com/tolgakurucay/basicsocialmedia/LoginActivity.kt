package com.tolgakurucay.basicsocialmedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.tolgakurucay.basicsocialmedia.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        val view=binding.root

        setContentView(view)


        buttonLogin.setOnClickListener {
           it?.let {


               startActivity(Intent(this,RegisterActivity::class.java))

           }
        }
    }




}