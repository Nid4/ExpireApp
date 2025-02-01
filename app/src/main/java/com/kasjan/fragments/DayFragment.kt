package com.kasjan.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.kasjan.R
import com.kasjan.activities.AddProductActivity
import com.kasjan.adapter.ProductsAdapter
import com.kasjan.databinding.FragmentDayBinding
import com.kasjan.model.AppDatabase
import com.kasjan.model.SharedViewModel
import com.kasjan.model.ShopProduct
import com.kasjan.utils.FirebaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class DayFragment : Fragment() {
    private var _binding: FragmentDayBinding? = null
    private val binding get() = _binding!! // Binding dostępny tylko między onCreateView a onDestroyView
    private lateinit var adapter: ProductsAdapter
    lateinit var viewModel: SharedViewModel
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDayBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicjalizacja ViewModel
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        // Inicjalizacja bazy danych
        db = AppDatabase.getDatabase(requireContext())
        setupFabListener()

        // Inicjalizacja RecyclerView i adaptera
        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProductsAdapter(mutableListOf()) { deletedProduct ->
            lifecycleScope.launch(Dispatchers.IO) {
                deleteProduct(deletedProduct)
                withContext(Dispatchers.Main) {
                    adapter.remove(deletedProduct)
                }
            }
        }
        binding.recyclerViewProducts.adapter = adapter

        // Obserwowanie zmiany daty w ViewModel
        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            updateDate(date)
        }

    }

    override fun onResume() {
        super.onResume()
        // Wymuszenie odświeżenia listy przy powrocie z AddActivity
        viewModel.selectedDate.value?.let { updateDate(it) }
    }

    fun updateDate(date: Date) {
        // Ustaw tekst daty
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        binding.textViewSelectedDate.text = dateFormat.format(date)
        Log.d("DayFragment", "Updating date to: ${dateFormat.format(date)}")

        // Pobierz produkty dla wybranej daty jako LiveData
        observeProductsForDate(date)
    }

    private fun observeProductsForDate(date: Date) {
        val calendar = Calendar.getInstance().apply { time = date }
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Obserwuj zmiany w Room
        db.productDao().getProductsByDate(day, month, year, userId)
            .observe(viewLifecycleOwner) { products ->
                adapter.update(products)
            }
    }

    private fun setupFabListener() {
        binding.fabAddProduct.setOnClickListener {
            val intent = Intent(requireContext(), AddProductActivity::class.java)
            startActivity(intent)
        }
    }

    private fun deleteProduct(product: ShopProduct) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                db.productDao().deleteProduct(product)
                Log.d("DayFragment", "Product deleted locally")

                val firebaseHelper = FirebaseHelper()
                firebaseHelper.deleteProductFromFirebase(product)
                Log.d("DayFragment", "Product deleted from Firebase")
            } catch (e: Exception) {
                Log.e("DayFragment", "Error deleting product from Firebase or local database", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Failed to delete product. Check your connection.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Unieważnienie bindingu, aby uniknąć wycieków pamięci
    }

    companion object {
        fun newInstance(): DayFragment {
            return DayFragment()
        }
    }
}



