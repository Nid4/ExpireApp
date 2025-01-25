package com.kasjan.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.kasjan.R
import com.kasjan.model.AppDatabase
import com.kasjan.model.ShopProduct
import com.kasjan.utils.FirebaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        deleteAllButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val db = AppDatabase.getDatabase(requireContext())
//                db.productDao().deleteAll()
                val firebaseHelper = FirebaseHelper()
                firebaseHelper.deleteAllProductsFromFirebase()
            }
        }

    }

}


