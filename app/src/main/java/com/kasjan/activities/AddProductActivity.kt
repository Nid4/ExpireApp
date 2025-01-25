package com.kasjan.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kasjan.databinding.ActivityProductAddBinding
import com.kasjan.model.AppDatabase
import com.kasjan.model.ShopProduct
import com.kasjan.utils.FirebaseHelper
import kotlinx.coroutines.launch
import java.util.*

class AddProductActivity : AppCompatActivity() {

    private var eanResult: String? = null // Variable to hold scanned EAN result
    private lateinit var binding: ActivityProductAddBinding
    private var selectedDay = 0
    private var selectedMonth = 0
    private var selectedYear = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Receive the EAN passed from the BarcodeReader activity
        eanResult = intent.getStringExtra("EAN")
        binding.editTextEAN.setText(eanResult)
        setButtonScaner()
        setDatePicker()
        setSaveButton()
    }

    private fun setDatePicker() {
        binding.buttonDatePicker.setOnClickListener { openDatePicker() }
    }

    private fun setSaveButton() {
        binding.buttonSaveProduct.setOnClickListener {
            val productName = binding.editTextProductName.text.toString()
            val productQuantity = binding.editTextProductQuantity.text.toString().toIntOrNull() ?: 0
            val productCategory = binding.editTextProductCategory.text.toString()
            val ean = binding.editTextEAN.text.toString().toIntOrNull() ?: 0

            // Check if required fields are filled
            if (TextUtils.isEmpty(productName) || selectedDay == 0 || selectedMonth == 0 || selectedYear == 0) {
                Toast.makeText(this, "Name and Expiry Date are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create product object
            val product = ShopProduct(
                ean = ean,
                name = productName,
                category = productCategory,
                quantity = productQuantity,
                day = selectedDay,
                month = selectedMonth,
                year = selectedYear
            )

            // Save product to Room and Firebase
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(this@AddProductActivity)
                db.productDao().insertProduct(product)

                val firebaseHelper = FirebaseHelper()
                firebaseHelper.addProductToFirebase(product)

                // Return to MainActivity and clear activity stack
                val intent = Intent(this@AddProductActivity, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setButtonScaner() {
        binding.buttonScanEan.setOnClickListener {
            startActivity(Intent(this, BarcodeReader::class.java))
            finish()
        }
    }

    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Save selected date
                this.selectedYear = selectedYear
                this.selectedMonth = selectedMonth + 1 // Months are 0-indexed
                this.selectedDay = selectedDay

                // Update TextView
                binding.editTextProductExpiryDate.setText("$selectedDay/$selectedMonth/$selectedYear")
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}
