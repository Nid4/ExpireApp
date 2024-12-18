package com.kasjan

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kasjan.databinding.ActivityResetPasswordLayoutBinding

class ResetPasswordActivity:AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicjalizacja View Binding
        binding = ActivityResetPasswordLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onResume() {
        super.onResume()
        binding.btnLogin.setOnClickListener {
            resetUserPassword()
        }
    }


    fun resetUserPassword(){
        var email =  binding.etUsername.text
        Toast.makeText(this,"Your $email was send to base, we working on it", Toast.LENGTH_LONG).show()

    }

}
