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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kasjan.R
import com.kasjan.activities.AddProductActivity
import com.kasjan.adapter.ProductsAdapter
import com.kasjan.model.AppDatabase
import com.kasjan.model.ShopProduct
import com.kasjan.utils.FirebaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class DayFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewDate: TextView
    private lateinit var faButton: FloatingActionButton
    private lateinit var adapter: ProductsAdapter
    private var currentSelectedDate: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewProducts)
        textViewDate = view.findViewById(R.id.textViewSelectedDate)
        faButton = view.findViewById(R.id.fabAddProduct)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFabListener()

        // Inicjalizacja RecyclerView i adaptera
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProductsAdapter(mutableListOf()) { deletedProduct ->
            lifecycleScope.launch(Dispatchers.IO) {
                deleteProduct(deletedProduct)
                withContext(Dispatchers.Main) {
                    adapter.remove(deletedProduct)
                }
            }
        }
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        val newDate = arguments?.getLong(ARG_DATE)?.let { Date(it) } ?: Date()
        if (currentSelectedDate == null || currentSelectedDate != newDate) {
            currentSelectedDate = newDate
            updateDate(newDate)
        }
    }

    fun updateDate(date: Date) {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        textViewDate.text = dateFormat.format(date)
        Log.d("DayFragment", "Updating date to: ${dateFormat.format(date)}")

        lifecycleScope.launch(Dispatchers.IO) {
            val products = getProductsForDate(date)
            withContext(Dispatchers.Main) {
                adapter.update(products) // Zaktualizuj dane w adapterze
            }
        }
    }

    private suspend fun getProductsForDate(date: Date): List<ShopProduct> {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)

        val db = AppDatabase.getDatabase(requireContext())

        // Pobierz dane lokalne
        val localProducts = db.productDao().getProductsByDate(day, month, year)

        if (localProducts.isNotEmpty()) {
            // Zwróć dane lokalne, jeśli są dostępne
            return localProducts
        } else {
            // Opcjonalne: spróbuj zsynchronizować z Firebase tylko w razie potrzeby
            val firebaseProducts = fetchProductsFromFirebase(day, month, year)
            saveProductsToLocalDatabase(firebaseProducts)
            return firebaseProducts
        }
    }

    private suspend fun fetchProductsFromFirebase(day: Int, month: Int, year: Int): List<ShopProduct> {
        // Implementacja zależna od Firebase SDK
        // Pobierz dane z Firebase i skonwertuj na List<ShopProduct>
        return emptyList() // Zastąp właściwą implementacją
    }

    private suspend fun saveProductsToLocalDatabase(products: List<ShopProduct>) {
        val db = AppDatabase.getDatabase(requireContext())
        db.productDao().insertAll(products) // insertProducts: @Insert(onConflict = REPLACE)
    }
    private fun setupFabListener() {
        faButton.setOnClickListener {
            val intent = Intent(requireContext(), AddProductActivity::class.java)
            startActivity(intent)
        }
    }

    private fun deleteProduct(product: ShopProduct) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val db = AppDatabase.getDatabase(requireContext())
                db.productDao().deleteProduct(product)
                Log.d("DayFragment", "Product deleted locally")

                val firebaseHelper = FirebaseHelper()
                firebaseHelper.deleteProductFromFirebase(product)
                Log.d("DayFragment", "Product deleted from Firebase")
            } catch (e: Exception) {
                Log.e("DayFragment", "Error deleting product from Firebase or local database", e)
                withContext(Dispatchers.Main) {
                    showErrorToast("Failed to delete product. Check your connection.")
                }
            }
        }
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val ARG_DATE = "arg_date"
        fun newInstance(date: Date?): DayFragment {
            val fragment = DayFragment()
            val args = Bundle()
            date?.let { args.putLong(ARG_DATE, it.time) }
            fragment.arguments = args
            return fragment
        }
    }
}

