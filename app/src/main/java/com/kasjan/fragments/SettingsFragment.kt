package com.kasjan.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextClock
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.kasjan.R
import com.kasjan.activities.LoginActivity
import com.kasjan.model.AppDatabase
import com.kasjan.model.ShopProduct
import com.kasjan.utils.FirebaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsFragment : Fragment() {
    private lateinit var deleteAllButton: Button
    private lateinit var logoutButton: Button
    private lateinit var textClock: TextClock
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        deleteAllButton = view.findViewById(R.id.delete_all_button)
        logoutButton = view.findViewById(R.id.logout_button)
        textClock = view.findViewById(R.id.textViewClock)
        logoutButton.setOnClickListener { logout() }
        setDeleteAllButtonListener()
        return view
    }

    private fun setDeleteAllButtonListener() {
        deleteAllButton.setOnClickListener { showDeleteAllDialog() }
        }

    private fun showDeleteAllDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete All Products")
            .setMessage("Are you sure you want to delete all products? This action cannot be undone.")
            .setPositiveButton("Delete") { dialog, _ ->
                deleteAllProducts()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

private fun deleteAllProducts() {
    lifecycleScope.launch(Dispatchers.IO) {
        try {
            // Usuń wszystkie produkty z lokalnej bazy danych
            val db = AppDatabase.getDatabase(requireContext())
            val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
            db.productDao().clearAllProducts(userId = userId)
            withContext(Dispatchers.Main) {
                Log.d("settingsFragment", "All products deleted successfully")
            }

            // Usuń wszystkie produkty z Firebase
            val firebaseHelper = FirebaseHelper()
            firebaseHelper.deleteAllProductsFromFirebase()
        } catch (e: Exception) {
            Log.e("settingsFragment", "Failed to delete all products", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Failed to delete all products", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}


