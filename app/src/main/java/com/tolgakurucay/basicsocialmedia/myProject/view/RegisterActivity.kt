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
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.tolgakurucay.basicsocialmedia.R
import com.tolgakurucay.basicsocialmedia.databinding.ActivityRegisterBinding
import kotlinx.android.synthetic.main.activity_login.buttonLogin
import kotlinx.android.synthetic.main.activity_phone_auth.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.concurrent.TimeUnit

class RegisterActivity : AppCompatActivity() {


    private lateinit var auth:FirebaseAuth
    private lateinit var binding:ActivityRegisterBinding
    private lateinit var storedVerificationId:String
    private lateinit var resendToken:PhoneAuthProvider.ForceResendingToken


    var phonNum=""
    var email=""
    var password=""
    var result:Boolean=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showAll()
        hideProgressBar()
        auth= FirebaseAuth.getInstance()

        buttonLogin.setOnClickListener {
            it?.let {

                startActivity(Intent(this, LoginActivity::class.java))
                onBackPressed()
                //firebase auth


            }
        }


        mailTextChangeListener()
        passwordChangeListener()
        phoneTextChangeListener()

    }


    override fun onBackPressed() {

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)

    }

    fun register(view: View){





        if(validateMailPasswordPhone()==true){
            showProgressBar()
            hideAll()
            phonNum=editTextPhoneNumber.text.toString()
            email=editTextEposta.text.toString()
            password=editTextParola.text.toString()

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

                    showAll()
                    hideProgressBar()
                }

            }

            sendVerificationCode(phonNum,callbacks)
        }
        else
        {
            Toast.makeText(this@RegisterActivity,"Bilgilerinizi Doğruladıktan Sonra Kayıt Olabilirsiniz",Toast.LENGTH_SHORT).show()
        }






    }

    private fun mailTextChangeListener(){
        binding.editTextEposta.addTextChangedListener {
            if(binding.editTextEposta.text.toString()=="")
            {
                binding.textInputLayoutEmail.error="Lütfen Bir E-Mail Adresi Giriniz"
            }
            else if(Patterns.EMAIL_ADDRESS.matcher(binding.editTextEposta.text.toString()).matches()!=true){
                binding.textInputLayoutEmail.error="Geçersiz E-Mail"
            }

            else
            {
                binding.textInputLayoutEmail.error=null
            }
        }

    }

    private fun passwordChangeListener(){
        binding.editTextParola.addTextChangedListener {
            if(binding.editTextParola.text.toString()==""){
                binding.textInputLayoutPassword.error="Lütfen Parola Giriniz\nParolada En Az 8 Karakter\nEn Az 1 Büyük Karakter\nEn Az 1 Küçük Karakter\nEn Az 1 Özel Karakter Zorunludur.\n\n"
            }
            else if(binding.editTextParola.text.toString().length<8){
                binding.textInputLayoutPassword.error="En Az 8 Karakter Girilmeli"
            }
            else if(!binding.editTextParola.text.toString().matches(".*[A-Z].*".toRegex())){
                binding.textInputLayoutPassword.error="En Az 1 Büyük Karakter Zorunludur"

            }
            else if(!binding.editTextParola.text.toString().matches(".*[a-z].*".toRegex())){
                binding.textInputLayoutPassword.error="En Az 1 Küçük Karakter Zorunludur"
            }
            else if(!binding.editTextParola.text.toString().matches(".*[@#\$%^&+=].*".toRegex())){
                binding.textInputLayoutPassword.error="En Az 1 Özel Karakter Kullanılmalıdır (@#\$%^&+=)"
            }
            else
            {
                binding.textInputLayoutPassword.error=null
            }
        }

    }

    private fun phoneTextChangeListener(){
        binding.editTextPhoneNumber.addTextChangedListener {
            if(binding.editTextPhoneNumber.text.toString()==""){
                binding.textInputLayoutPhone.error="Lütfen Cep Numaranızı Giriniz"
            }
            else if(binding.editTextPhoneNumber.text.toString().length<10){
                binding.textInputLayoutPhone.error="Lütfen Cep Telefonunuzu 10 Haneli Giriniz"

            }


            else
            {
                binding.textInputLayoutPhone.error=null
            }
        }



    }

    private fun validateMailPasswordPhone() : Boolean{
        val mailIsTrue=binding.textInputLayoutEmail.error
        val passwordIsTrue=binding.textInputLayoutPassword.error
        val phoneIsTrue=binding.textInputLayoutPhone.error
        if(mailIsTrue==null && passwordIsTrue==null && phoneIsTrue==null){
            return true
        }
        return false


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






}