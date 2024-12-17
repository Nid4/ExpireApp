package com.kasjan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kasjan.databinding.ActivityLoginLayoutBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicjalizacja View Binding
        binding = ActivityLoginLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sprawdzanie, czy użytkownik jest już zalogowany
        val isLoggedIn = checkIfUserLoggedIn()
        if (isLoggedIn) {
            navigateToMainActivity()
            finish()
            return
        }

        // Obsługa kliknięcia przycisku logowania
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Wprowadź login i hasło", Toast.LENGTH_SHORT).show()
            } else {
                if (validateCredentials(username, password)) {
                    saveLoginState()
                    navigateToMainActivity()
                } else {
                    Toast.makeText(this, "Nieprawidłowe dane logowania", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Obsługa kliknięcia "Zapomniałeś hasła?"
        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Opcja odzyskiwania hasła w przygotowaniu", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun checkIfUserLoggedIn(): Boolean {
        // Tymczasowe sprawdzanie stanu logowania (np. poprzez SharedPreferences)
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    private fun saveLoginState() {
        // Zapisywanie stanu logowania (np. do SharedPreferences)
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("is_logged_in", true)
            apply()
        }
    }

    private fun validateCredentials(username: String, password: String): Boolean {
        // Prosta weryfikacja (można zastąpić prawdziwą weryfikacją z serwera lub bazy danych)
        return username == "admin" && password == "admin123"
    }

    private fun navigateToMainActivity() {
        // Nawigacja do głównej aktywności
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
