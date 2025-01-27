package com.kasjan.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.kasjan.R
import com.kasjan.model.AppDatabase
import com.kasjan.model.ShopProduct
import com.kasjan.utils.FirebaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsFragment : Fragment() {
    private lateinit var deleteAllButton: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        deleteAllButton = view.findViewById(R.id.delete_all_button)
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
            db.productDao().clearAllProducts()
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

}


