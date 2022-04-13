package com.tolgakurucay.basicsocialmedia.myProject.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.tolgakurucay.basicsocialmedia.R
import com.tolgakurucay.basicsocialmedia.databinding.FragmentCreateUserBinding
import com.tolgakurucay.basicsocialmedia.util.Variables
import com.tolgakurucay.basicsocialmedia.viewmodel.CreateUserViewModel

class CreateUserFragment : Fragment() {



    private lateinit var binding:FragmentCreateUserBinding
    private lateinit var viewModel:CreateUserViewModel
    var selectedImage: Uri?=null
    var selectedImageString: String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding=FragmentCreateUserBinding.bind(view)
        binding.textViewEmail.setText(Variables.vemail)
        binding.textViewPassword.setText(Variables.vpassword)
        binding.textViewPhone.setText(Variables.vphoneNumber)

        viewModel=ViewModelProvider(this).get(CreateUserViewModel::class.java)


        binding.imageView.setOnClickListener {
            showProgressBar()
            hideAll()
            val intent=Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,3)

        }

        binding.buttonSave.setOnClickListener {
            if(viewModel.validateNameAndSurname(binding.editTextName.text.toString(),binding.editTextSurname.text.toString())){
                Variables.vimagestring=selectedImageString
                Variables.vname=binding.editTextName.text.toString()
                Variables.vsurname=binding.editTextSurname.text.toString()
                val isSaved=viewModel.saveDataToFirebase(Variables.vname,Variables.vsurname,Variables.vimagestring,Variables.vphoneNumber,Variables.vemail)
                if(isSaved){
                    Toast.makeText(it.context,"Veriler Kaydedildi",Toast.LENGTH_SHORT).show()

                }
                else
                {
                    Toast.makeText(it.context,"Veriler Kaydedilemedi!",Toast.LENGTH_SHORT).show()
                }
            }
            else{

                Toast.makeText(it.context,"Lütfen Ad ve Soyad Kısımlarını Doldurunuz",Toast.LENGTH_SHORT).show()
            }

        }

        binding.editTextName.addTextChangedListener {
            binding.textNameContainer.helperText=viewModel.validateName(binding.editTextName.text.toString())




        }
        binding.editTextSurname.addTextChangedListener {
            binding.textSurnameContainer.helperText=viewModel.validateSurname(binding.editTextSurname.text.toString())

            }





    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let {
            if(resultCode==RESULT_OK){
                 selectedImage=data.data
                selectedImageString=data.dataString
                binding.imageView.setImageURI(selectedImage)
                hideProgressBar()
                showAll()
            }
        }

    }
    private fun showProgressBar(){
        binding.progressBarCreateUser.visibility=View.VISIBLE

    }
    private fun hideProgressBar(){
        binding.progressBarCreateUser.visibility=View.INVISIBLE

    }
    private fun showAll(){
        binding.imageView.visibility=View.VISIBLE
        binding.linearLayout.visibility=View.VISIBLE
        binding.linearLayout2.visibility=View.VISIBLE
        binding.buttonSave.visibility=View.VISIBLE
        binding.textNameContainer.visibility=View.VISIBLE
        binding.textSurnameContainer.visibility=View.VISIBLE

    }
    private fun hideAll(){
        binding.imageView.visibility=View.INVISIBLE
        binding.linearLayout.visibility=View.INVISIBLE
        binding.linearLayout2.visibility=View.INVISIBLE
        binding.buttonSave.visibility=View.INVISIBLE
        binding.textNameContainer.visibility=View.INVISIBLE
        binding.textSurnameContainer.visibility=View.INVISIBLE

    }

}