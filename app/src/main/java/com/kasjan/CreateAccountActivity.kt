package com.kasjan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kasjan.databinding.ActivityCreateAccountBinding
import com.kasjan.databinding.ActivityResetPasswordLayoutBinding

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
