package com.tolgakurucay.basicsocialmedia.myProject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.firebase.firestore.FirebaseFirestore

import com.tolgakurucay.basicsocialmedia.R
import com.tolgakurucay.basicsocialmedia.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_login.*

import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.tolgakurucay.basicsocialmedia.util.Variables

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding= ActivityLoginBinding.inflate(layoutInflater)
        val view=binding.root

        setContentView(view)
        firestore= FirebaseFirestore.getInstance()
        auth= FirebaseAuth.getInstance()
        auth.useAppLanguage()

       auth.currentUser?.let {
           val intent=Intent(this,FeedActivity::class.java)
           Variables.oldAccount=true
           startActivity(intent)
       }


       //binding.btnKayitOl.visibility=View.INVISIBLE


        buttonLogin.setOnClickListener {
           it?.let {


              val intent= Intent(this, RegisterActivity::class.java)
               startActivity(intent)
               overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)



           }
        }


        btnKayitOl.setOnClickListener { view->

        if(validateMailAndPassword()==true){

            firestore= FirebaseFirestore.getInstance()
            firestore.collection("users")
                .whereEqualTo("email",binding.textEmail.text.toString())
                .whereEqualTo("password",binding.textPassword.text.toString())
                .get()
                .addOnSuccessListener {
                    if(!it.isEmpty){
                        Toast.makeText(this,"Giriş Yapıldı",Toast.LENGTH_SHORT).show()
                        val intent=Intent(this,FeedActivity::class.java)
                       Variables.oldAccount=true
                        startActivity(intent)
                    }
                    else
                    {
                        Toast.makeText(this,"E-Mail ve Şifre Uyuşmuyor",Toast.LENGTH_SHORT).show()
                    }
                }




            //firebase doğrulamasından sonra


        }

            else
        {
            Toast.makeText(this@LoginActivity,"Lütfen Bilgilerinizi Kontrol Ediniz",Toast.LENGTH_SHORT).show()

        }



        }
        mailTextChangeListener()
        passwordChangeListener()









    }

    private fun validateMailAndPassword():Boolean{
        val emailIsTrue=binding.inputLayoutEmail.helperText
        val passwordIsTrue=binding.inputLayoutPassword.helperText
        if(emailIsTrue==null && passwordIsTrue==null){
            return true

        }
        return false

    }



    private fun mailTextChangeListener(){
        binding.textEmail.addTextChangedListener {
            if(binding.textEmail.text.toString()=="")
            {
                binding.inputLayoutEmail.helperText="Lütfen Bir E-Mail Adresi Giriniz"
            }
            else if(Patterns.EMAIL_ADDRESS.matcher(binding.textEmail.text.toString()).matches()!=true){
                binding.inputLayoutEmail.helperText="Geçersiz E-Mail"
            }

            else
            {
                binding.inputLayoutEmail.helperText=null
            }
        }

    }
    private fun passwordChangeListener(){
        binding.textPassword.addTextChangedListener {
            if(binding.textPassword.text.toString()==""){
                binding.inputLayoutPassword.helperText="Lütfen Parola Giriniz\nParolada En Az 8 Karakter\nEn Az 1 Büyük Karakter\nEn Az 1 Küçük Karakter\nEn Az 1 Özel Karakter Zorunludur.\n\n"
            }
            else if(binding.textPassword.text.toString().length<8){
                binding.inputLayoutPassword.helperText="En Az 8 Karakter Girilmeli"
            }
           else if(!binding.textPassword.text.toString().matches(".*[A-Z].*".toRegex())){
                binding.inputLayoutPassword.helperText="En Az 1 Büyük Karakter Zorunludur"

            }
            else if(!binding.textPassword.text.toString().matches(".*[a-z].*".toRegex())){
                binding.inputLayoutPassword.helperText="En Az 1 Küçük Karakter Zorunludur"
            }
           else if(!binding.textPassword.text.toString().matches(".*[@#\$%^&+=].*".toRegex())){
                binding.inputLayoutPassword.helperText="En Az 1 Özel Karakter Kullanılmalıdır (@#\$%^&+=)"
            }
            else
            {
                binding.inputLayoutPassword.helperText=null
            }
        }

    }












}