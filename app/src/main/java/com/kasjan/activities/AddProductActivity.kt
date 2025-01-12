package com.kasjan.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kasjan.databinding.ActivityProductAddBinding
import com.kasjan.model.AppDatabase
import com.kasjan.model.ShopProduct
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddProductActivity : AppCompatActivity() {

    private var eanResult: String? = null // Variable to hold scanned EAN result
    private lateinit var binding: ActivityProductAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Receive the EAN passed from the BarcodeReader activity
        eanResult = intent.getStringExtra("EAN")
        binding.editTextEAN.setText(eanResult)

        setButtonScaner()
        setSaveButton()
    }

    private fun setSaveButton() {
        binding.buttonSaveProduct.setOnClickListener {
            val productName = binding.editTextProductName.text.toString()
            val productQuantity = binding.editTextProductQuantity.text.toString().toIntOrNull() ?: 0
            val productExpiryDate = binding.editTextProductExpiryDate.text.toString()
            val productCategory = binding.editTextProductCategory.text.toString()
            val ean = binding.editTextEAN.text.toString().toIntOrNull() ?: 0
            // Make sure all required fields are filled in
            if (TextUtils.isEmpty(productName) || TextUtils.isEmpty(productExpiryDate) || TextUtils.isEmpty(productCategory)) {
                // You could show a Toast or some error message here
                return@setOnClickListener
            }

            // Parse the expiry date
            val expiryDate = try {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(productExpiryDate) ?: Date()
            } catch (e: Exception) {
                // Handle invalid date format
                Date()
            }

            // Zapisz produkt do bazy
            val product = ShopProduct(
                ean = ean,
                name = productName,
                category = productCategory,
                quantity = productQuantity,
                expiryDate = expiryDate
            )
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(this@AddProductActivity)
                db.productDao().insertProduct(product)
                // Wróć do MainActivity i usuń pozostałe aktywności ze stosu
                val intent = Intent(this@AddProductActivity, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setButtonScaner() {
        binding.buttonScanEan.setOnClickListener { startActivity(Intent(this, BarcodeReader::class.java))
        finish()
        }
    }

}
