package com.tolgakurucay.basicsocialmedia.myProject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.*
import com.tolgakurucay.basicsocialmedia.databinding.ActivityPhoneAuthBinding
import kotlinx.android.synthetic.main.activity_phone_auth.*
import android.view.View
import com.google.firebase.FirebaseException
import com.tolgakurucay.basicsocialmedia.util.Variables
import java.util.concurrent.TimeUnit


class PhoneAuthActivity : AppCompatActivity() {


    private lateinit var binding: ActivityPhoneAuthBinding
    private  lateinit var auth:FirebaseAuth
    var callBacks:PhoneAuthProvider.OnVerificationStateChangedCallbacks?=null



    var email:String?=null
    var phone:String?=null
    var password:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityPhoneAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()

         email=intent.getStringExtra("email")
         phone=intent.getStringExtra("phone")
         password=intent.getStringExtra("password")
        var storedVerificationId=intent.getStringExtra("storedVerificationId")
        val token=intent.getParcelableExtra<PhoneAuthProvider.ForceResendingToken>("token")


        buttonVerify.setOnClickListener { view->
            hideAll()
            showProgressBar()
            var otbCode=editTextNumber.text.toString()
            if(otbCode.isNotEmpty()){
                var credential:PhoneAuthCredential=PhoneAuthProvider.getCredential(storedVerificationId!!,otbCode)
                signInWithAuthCredential(credential)
            }
            else
            {
                Toast.makeText(this@PhoneAuthActivity,"Lütfen Gönderilen Kodu Giriniz",Toast.LENGTH_SHORT).show()
            }

        }

        textViewNotSent.setOnClickListener { view->
            hideAll()
            showProgressBar()

            val options=PhoneAuthOptions.newBuilder()
                .setTimeout(60L,TimeUnit.SECONDS)
                .setCallbacks(callBacks!!)
                .setPhoneNumber(phone!!)
                .setForceResendingToken(token!!)
                .setActivity(this@PhoneAuthActivity)
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)

        }






         callBacks= object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                hideProgressBar()
                showAll()
                Toast.makeText(this@PhoneAuthActivity,"Doğrulama Başarılı",Toast.LENGTH_SHORT).show()
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                hideProgressBar()
                showAll()
                Toast.makeText(this@PhoneAuthActivity,"Doğrulama Başarısız",Toast.LENGTH_SHORT).show()
            }

             override fun onCodeSent(storedVerifId: String, p1: PhoneAuthProvider.ForceResendingToken) {
                 hideProgressBar()
                 showAll()
                 Toast.makeText(this@PhoneAuthActivity,"Kod Tekrardan Gönderildi",Toast.LENGTH_SHORT).show()
                 storedVerificationId=storedVerifId


             }

        }






    }



    private fun signInWithAuthCredential(credential: PhoneAuthCredential){
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    Toast.makeText(this@PhoneAuthActivity,"Giriş Başarılı",Toast.LENGTH_SHORT).show()
                    hideProgressBar()
                    showAll()
                    val intent=Intent(this@PhoneAuthActivity,FeedActivity::class.java)
                    intent.putExtra("phone",phone)
                    intent.putExtra("password",password)
                    intent.putExtra("email",email)
                    intent.putExtra("from","newAccount")

                    Variables.vemail=email
                    Variables.vphoneNumber=phone
                    Variables.vpassword=password
                    startActivity(intent)


                }
                else
                {
                    if(task.exception is FirebaseAuthInvalidCredentialsException){
                        Toast.makeText(this@PhoneAuthActivity,"Geçersiz Kod",Toast.LENGTH_SHORT).show()


                    }
                }

            }

        hideProgressBar()
        showAll()
    }


    private fun showProgressBar(){
        progressBarPhoneAuth.visibility=View.VISIBLE

    }
    private fun hideProgressBar(){
        progressBarPhoneAuth.visibility=View.INVISIBLE

    }
    private fun showAll(){
        imageView3.visibility=View.VISIBLE
        textViewNotSent.visibility=View.VISIBLE
        buttonVerify.visibility=View.VISIBLE
        editTextNumber.visibility=View.VISIBLE
        textView3.visibility=View.VISIBLE

    }
    private fun hideAll(){
        imageView3.visibility=View.INVISIBLE
        textViewNotSent.visibility=View.INVISIBLE
        buttonVerify.visibility=View.INVISIBLE
        editTextNumber.visibility=View.INVISIBLE
        textView3.visibility=View.INVISIBLE

    }






}






























