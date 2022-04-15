package com.tolgakurucay.basicsocialmedia.viewmodel

import android.graphics.Bitmap

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tolgakurucay.basicsocialmedia.model.NewUserModel
import com.tolgakurucay.basicsocialmedia.util.Variables


class CreateUserViewModel:ViewModel(){

    val bool=MutableLiveData<Boolean>()


    fun saveDataToFirebase(user:NewUserModel){

        var firebase=FirebaseFirestore.getInstance()
        firebase.collection("users").add(user)
            .addOnCompleteListener {
            bool.value = true
        }
            .addOnFailureListener {
                bool.value = false
                println(it.localizedMessage)
            }




    }



    fun validateName(name:String) : String?{
        if(name==""){
            return "Lütfen İsminizi Giriniz"
        }
        return null

    }
    fun validateSurname(surname:String):String?{
        if(surname==""){
            return "Lütfen Soyisminizi Giriniz"
        }
        return null

    }

    fun validateNameAndSurname(name:String,surname:String) : Boolean{
        if(validateName(name)==null && validateSurname(surname)==null){
            return true
        }
        return false

    }





}