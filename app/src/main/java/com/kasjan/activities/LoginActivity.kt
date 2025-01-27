package com.kasjan.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.kasjan.databinding.ActivityLoginLayoutBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginLayoutBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicjalizacja FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Sprawdzanie, czy użytkownik jest już zalogowany
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            navigateToMainActivity()
            finish()
            return
        }

        loginButton()
        createAccountButton()
        resetButton()
    }

    private fun loginButton() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Wprowadź adres e-mail i hasło", Toast.LENGTH_SHORT).show()

            }

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        navigateToMainActivity()
                    } else {
                        Toast.makeText(this, "Nieprawidłowe dane logowania", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun createAccountButton() {
        binding.textViewCreateAccount.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }
    }

    private fun resetButton() {
        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
