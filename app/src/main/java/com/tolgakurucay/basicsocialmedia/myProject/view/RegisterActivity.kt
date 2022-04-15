package com.tolgakurucay.basicsocialmedia.myProject.view

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.hbb20.CountryCodePicker
import com.tolgakurucay.basicsocialmedia.R
import com.tolgakurucay.basicsocialmedia.databinding.ActivityRegisterBinding
import com.tolgakurucay.basicsocialmedia.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.activity_login.buttonLogin
import kotlinx.android.synthetic.main.activity_phone_auth.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.concurrent.TimeUnit

class RegisterActivity : AppCompatActivity() {


    private lateinit var auth:FirebaseAuth
    private lateinit var binding:ActivityRegisterBinding
    private lateinit var storedVerificationId:String
    private lateinit var resendToken:PhoneAuthProvider.ForceResendingToken
    private lateinit var viewModel:RegisterViewModel


    var phonNum=""
    var email=""
    var password=""
    var result:Boolean=false

    var isUserCreated:Boolean?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //viewBinding
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Initialize ViewModel and firebase auth
        viewModel=ViewModelProvider(this).get(RegisterViewModel::class.java)
        auth= FirebaseAuth.getInstance()
        //

        showAll()
        hideProgressBar()


        buttonLogin.setOnClickListener {
            it?.let {

                startActivity(Intent(this, LoginActivity::class.java))
                onBackPressed()



            }
        }


        mailTextChangeListener()
        passwordChangeListener()
        phoneTextChangeListener()

        observeLiveData()

    }


    override fun onBackPressed() {

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)

    }

    fun register(view: View){


            viewModel.validatePhoneMailPassword()
            if(result==true){
                hideAll()
                showProgressBar()
                phonNum=editTextPhoneNumber.text.toString()
                email=editTextEposta.text.toString()
                password=editTextParola.text.toString()



               viewModel.isDataInFirebase(countryCodePicker.selectedCountryCodeWithPlus+phonNum)



                //viewModel.sendVerificationCode(countryCodePicker.selectedCountryCodeWithPlus+phonNum,auth,this@RegisterActivity)
            }





            else
            {
                Toast.makeText(this@RegisterActivity,"Bilgilerinizi Doğruladıktan Sonra Kayıt Olabilirsiniz",Toast.LENGTH_SHORT).show()
            }















    }

    private fun mailTextChangeListener(){
        binding.editTextEposta.addTextChangedListener {
            viewModel.mailValidation(binding.editTextEposta.text.toString())
        }

    }

    private fun passwordChangeListener(){
        binding.editTextParola.addTextChangedListener {
           viewModel.passwordValidation(binding.editTextParola.text.toString())

        }

    }

    private fun phoneTextChangeListener(){
        binding.editTextPhoneNumber.addTextChangedListener {
            viewModel.phoneValidation(binding.editTextPhoneNumber.text.toString())

        }



    }





    private fun sendVerificationCode(phoneNumber:String,callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks){
        val options= PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(countryCodePicker.selectedCountryCodeWithPlus+phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this@RegisterActivity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)

    }


    private fun hideAll(){
        cardView.visibility=View.GONE
        buttonLogin.visibility=View.GONE
        view.visibility=View.GONE
        btnKayitOl.visibility=View.GONE
        textView.visibility=View.GONE

    }
    private fun showAll(){
        cardView.visibility=View.VISIBLE
        buttonLogin.visibility=View.VISIBLE
        view.visibility=View.VISIBLE
        btnKayitOl.visibility=View.VISIBLE
        textView.visibility=View.VISIBLE

    }
    private fun showProgressBar(){
        progressBar2.visibility=View.VISIBLE

    }
    private fun hideProgressBar(){
        progressBar2.visibility=View.GONE
    }


    private fun observeLiveData(){
        viewModel.mailMessage.observe(this@RegisterActivity, Observer { mailMessage->
            binding.textInputLayoutEmail.helperText=mailMessage

        })

        viewModel.passwordMessage.observe(this@RegisterActivity, Observer { passwordMessage->
            binding.textInputLayoutPassword.helperText=passwordMessage

        })
        viewModel.phoneMessage.observe(this@RegisterActivity, Observer { phoneMessage->
            binding.textInputLayoutPhone.helperText=phoneMessage

        })
        viewModel.validateAll.observe(this@RegisterActivity, Observer { isValidated->
            result=isValidated

        })
        viewModel.isDataInFirebase.observe(this@RegisterActivity, Observer { isIn->

            if (isIn){
                Toast.makeText(this,"Bu Telefon Numarasıyla Bir Kayıt Var",Toast.LENGTH_SHORT).show()
                showAll()
                hideProgressBar()

            }
            else
            {




                var callbacks=object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        Toast.makeText(this@RegisterActivity,"Doğrulama Başarılı",Toast.LENGTH_SHORT).show()
                        progressBar2.visibility=View.GONE
                        showAll()
                        hideProgressBar()

                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        progressBar2.visibility=View.GONE
                        Toast.makeText(this@RegisterActivity,"Giriş Başarısız, Hata:"+p0.localizedMessage,Toast.LENGTH_SHORT).show()
                        println(p0.localizedMessage)
                        showAll()
                        hideProgressBar()
                    }

                    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                        showAll()
                        hideProgressBar()
                        Toast.makeText(this@RegisterActivity,"Doğrulama Kodu Gönderildi",Toast.LENGTH_SHORT).show()
                        storedVerificationId=verificationId
                        resendToken=token
                        val intent=Intent(this@RegisterActivity,PhoneAuthActivity::class.java)
                        //gönderilecekler
                        val phoneWithCountry=countryCodePicker.selectedCountryCodeWithPlus+phonNum


                        intent.putExtra("phone",phoneWithCountry)
                        intent.putExtra("email",email)
                        intent.putExtra("password",password)
                        intent.putExtra("storedVerificationId",storedVerificationId)
                        intent.putExtra("token",resendToken)
                        startActivity(intent)


                    }

                }



                sendVerificationCode(phonNum,callbacks)
            }


        })


    }






}