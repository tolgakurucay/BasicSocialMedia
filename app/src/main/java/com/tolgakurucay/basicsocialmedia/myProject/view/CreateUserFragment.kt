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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.tolgakurucay.basicsocialmedia.R
import com.tolgakurucay.basicsocialmedia.databinding.FragmentCreateUserBinding
import com.tolgakurucay.basicsocialmedia.model.NewUserModel
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
        val view= inflater.inflate(R.layout.fragment_create_user, container, false)
        // Inflate the layout for this fragment

        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(Variables.oldAccount==true){
            val action=CreateUserFragmentDirections.actionCreateUserFragmentToFeedFragment()
            Navigation.findNavController(view).navigate(action)
        }


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
                showProgressBar()
                hideAll()
                Variables.vimagestring=selectedImageString
                Variables.vimageUri=selectedImage
                Variables.vname=binding.editTextName.text.toString()
                Variables.vsurname=binding.editTextSurname.text.toString()

                val user=NewUserModel(binding.editTextName.text.toString(),binding.editTextSurname.text.toString(),Variables.vphoneNumber,Variables.vemail,Variables.vimagestring,Variables.vpassword)

                if(selectedImage!=null){
                    viewModel.saveDataToFirebase(user)

                }
                else
                {
                    Toast.makeText(it.context,"Resim Seçiniz",Toast.LENGTH_SHORT).show()
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


observeLiveData()


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


    private fun observeLiveData(){
        viewModel.bool.observe(viewLifecycleOwner, Observer {
            if(it){
                println("kaydedildi")
                Toast.makeText(this.context,"Veriler Kaydedildi",Toast.LENGTH_SHORT).show()
                val action=CreateUserFragmentDirections.actionCreateUserFragmentToFeedFragment()
                Navigation.findNavController(this@CreateUserFragment.requireView()).navigate(action)
                hideProgressBar()
                showAll()
            }
            else
            {
                println("kaydedilmedi")
            }
        })
    }

}