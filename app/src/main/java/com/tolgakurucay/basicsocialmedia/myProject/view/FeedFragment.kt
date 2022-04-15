package com.tolgakurucay.basicsocialmedia.myProject.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.tolgakurucay.basicsocialmedia.R
import com.tolgakurucay.basicsocialmedia.databinding.FragmentFeedBinding
import com.tolgakurucay.basicsocialmedia.util.Variables
import com.tolgakurucay.basicsocialmedia.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : Fragment() {

    private lateinit var binding:FragmentFeedBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var viewModel:FeedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_feed, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel=ViewModelProvider(this).get(FeedViewModel::class.java)
        binding= FragmentFeedBinding.bind(view)
        auth= FirebaseAuth.getInstance()


        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.logout->logout()
                // R.id.chat->goTo()


            }
            true

        }

        binding.button.setOnClickListener {


        }





    }

    private fun logout(){
        auth.signOut()
        val action=FeedFragmentDirections.actionFeedFragmentToLoginActivity()
        Navigation.findNavController(this.requireView()).navigate(action)
    }





}