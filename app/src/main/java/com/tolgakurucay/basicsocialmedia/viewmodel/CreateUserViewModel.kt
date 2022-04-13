package com.tolgakurucay.basicsocialmedia.viewmodel

import android.graphics.Bitmap

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore


class CreateUserViewModel:ViewModel(){

    var nameMutable = MutableLiveData<String>()

    var surnameMutable = MutableLiveData<String>()

    var imageBitmapMutable = MutableLiveData<Bitmap>()


    fun saveDataToFirebase(name:String?,surname:String?,imageString: String?,phoneNumber:String?,email:String?) : Boolean{
         var boolean=true
        val firestore=FirebaseFirestore.getInstance()
        val user= hashMapOf(
            "name" to name,
            "surname" to surname,
            "phone" to phoneNumber,
            "email" to email,
            "imageString" to imageString
        )
        firestore.collection(name+"_"+surname).add(user).addOnCompleteListener {
            boolean=true
            println("completeList")

        }
            .addOnFailureListener {
                println(it.localizedMessage)
                boolean=true
                println("failList")
            }
            .addOnSuccessListener {
                boolean=true
                println("successList")
            }
        println(boolean)
        return boolean



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