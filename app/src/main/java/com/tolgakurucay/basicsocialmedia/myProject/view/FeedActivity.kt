package com.tolgakurucay.basicsocialmedia.myProject.view
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tolgakurucay.basicsocialmedia.databinding.ActivityFeedBinding
class FeedActivity : AppCompatActivity() {






    private lateinit var binding:ActivityFeedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var from=intent.getStringExtra("from")




        if(from=="newAccount"){
            println("Yeni Hesap")



        }
        else if(from=="oldAccount")
        {
            println("Eski Hesap")
        }




    }
}