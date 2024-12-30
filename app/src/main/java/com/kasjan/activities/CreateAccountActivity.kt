package com.kasjan.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kasjan.databinding.ActivityCreateAccountBinding

class CreateAccountActivity:AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onResume() {
        super.onResume()
        binding.btnLogin.setOnClickListener {
            CreateAccount()
        }
    }

    fun CreateAccount(){
       binding

    }
    fun goToRegulamin() {
        val intent = Intent(this, RegulaminActivity::class.java)
        startActivity(intent)
        finish()
    }

}
