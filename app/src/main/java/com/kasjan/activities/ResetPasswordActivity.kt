package com.kasjan.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.kasjan.databinding.ActivityResetPasswordLayoutBinding

class ResetPasswordActivity:AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordLayoutBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordLayoutBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener {
            var email =  binding.etUsername.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, "Provide E-mail ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "E-mail to restart password has been sent", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to send e-mail", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

}

