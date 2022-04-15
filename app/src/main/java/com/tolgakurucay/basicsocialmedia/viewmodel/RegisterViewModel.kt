package com.tolgakurucay.basicsocialmedia.viewmodel

import android.app.Activity
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.tolgakurucay.basicsocialmedia.myProject.view.RegisterActivity
import kotlinx.android.synthetic.main.activity_register.*
import java.util.concurrent.TimeUnit


class RegisterViewModel:ViewModel() {

    val mailMessage=MutableLiveData<String?>()
    val passwordMessage=MutableLiveData<String?>()
    val phoneMessage=MutableLiveData<String?>()
    val validateAll=MutableLiveData<Boolean>()
    val isSendVerifCode=MutableLiveData<Boolean>()
    val isDataInFirebase=MutableLiveData<Boolean>()



    var firebase= FirebaseFirestore.getInstance()



    fun isDataInFirebase(phoneNumber:String) {



        firebase.collection("users")
            .whereEqualTo("phone",phoneNumber)
            .get()
            .addOnSuccessListener { users->
                if(!users.isEmpty){

                    isDataInFirebase.value=true
                }
                else
                {
                    isDataInFirebase.value=false
                }


            }
            .addOnFailureListener {

            }




    }


    fun mailValidation(email:String){
        if(email=="")
        {
            mailMessage.value= "Lütfen Bir E-Mail Adresi Giriniz"
        }
        else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()!=true){
            mailMessage.value= "Geçersiz E-Mail"
        }

        else
        {
            mailMessage.value= null
        }
    }

    fun passwordValidation(password:String){
        if(password==""){
            passwordMessage.value= "Lütfen Parola Giriniz\nParolada En Az 8 Karakter\nEn Az 1 Büyük Karakter\nEn Az 1 Küçük Karakter\nEn Az 1 Özel Karakter Zorunludur.\n\n"
        }
        else if(password.length<8){
            passwordMessage.value= "En Az 8 Karakter Girilmeli"
        }
        else if(!password.matches(".*[A-Z].*".toRegex())){
            passwordMessage.value= "En Az 1 Büyük Karakter Zorunludur"

        }
        else if(!password.matches(".*[a-z].*".toRegex())){
            passwordMessage.value= "En Az 1 Küçük Karakter Zorunludur"
        }
        else if(!password.matches(".*[@#\$%^&+=].*".toRegex())){
            passwordMessage.value= "En Az 1 Özel Karakter Kullanılmalıdır (@#\$%^&+=)"
        }
        else
        {
            passwordMessage.value= null
        }

    }

    fun phoneValidation(phone:String){
        if(phone==""){
           phoneMessage.value= "Lütfen Cep Numaranızı Giriniz"
        }
        else if(phone.length<10){
            phoneMessage.value= "Lütfen Cep Telefonunuzu 10 Haneli Giriniz"

        }


        else
        {
            phoneMessage.value=null
        }
    }

    fun validatePhoneMailPassword(){
        validateAll.value = phoneMessage.value==null && mailMessage.value==null && passwordMessage.value==null


    }

     fun sendVerificationCode(phoneNumberWithPlus:String,auth:FirebaseAuth,activity: Activity){


         val callbacks=object  : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
             override fun onVerificationCompleted(p0: PhoneAuthCredential) {

             }

             override fun onVerificationFailed(p0: FirebaseException) {

             }

             override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {

                 isSendVerifCode.value=true

             }

         }



        val options= PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumberWithPlus)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)

    }







}