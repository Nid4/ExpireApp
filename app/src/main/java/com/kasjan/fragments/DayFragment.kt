package com.kasjan.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kasjan.activities.AddProductActivity
import com.kasjan.R
import com.kasjan.adapter.ProductsAdapter
import com.kasjan.model.AppDatabase
import com.kasjan.model.ShopProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class DayFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewDate: TextView
    private lateinit var faButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewProducts)
        textViewDate = view.findViewById(R.id.textViewSelectedDate)
        faButton = view.findViewById(R.id.fabAddProduct)

        val initialDate = arguments?.getLong(ARG_DATE)?.let { Date(it) } ?: Date()
        updateDate(initialDate)

        return view
    }

    fun updateDate(date: Date) {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        textViewDate.text = dateFormat.format(date)

        // Pobierz produkty i zaktualizuj UI w tle
        lifecycleScope.launch(Dispatchers.IO) {
            val products = getProductsForDate(date)
            withContext(Dispatchers.Main) {
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = ProductsAdapter(products)
            }
        }
    }

    private suspend fun getProductsForDate(date: Date): List<ShopProduct> {
        val calendar = Calendar.getInstance()
        calendar.time = date

        // Ustawienie zakresu dnia
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startDate = calendar.time

        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val endDate = calendar.time

        val db = AppDatabase.getDatabase(requireContext())
        return db.productDao().getProductsByDateRange(startDate, endDate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFabListener()
    }

    private fun setupFabListener() {
        faButton.setOnClickListener {
            val intent = Intent(requireContext(), AddProductActivity::class.java)
            startActivity(intent)
        }
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
