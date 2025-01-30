package com.kasjan.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
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

            if (TextUtils.isEmpty(productName) || selectedDay == 0 || selectedMonth == 0 || selectedYear == 0) {
                Toast.makeText(this, "Name and Expiry Date are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val product = ShopProduct(
                ean = ean,
                name = productName,
                category = productCategory,
                quantity = productQuantity,
                day = selectedDay,
                month = selectedMonth,
                year = selectedYear,
                userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            )

            lifecycleScope.launch {
                saveProduct(product)
                val intent = Intent(this@AddProductActivity, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
                startActivity(intent)
                finish()
            }
        }
    }
    private suspend fun saveProduct(product: ShopProduct) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val db = AppDatabase.getDatabase(this@AddProductActivity)
        val firebaseHelper = FirebaseHelper()

        if (currentUser != null) {
            val userId = currentUser.uid
            val productWithUser = product.copy( userId = userId)
            // Zapisz produkt w Room i pobierz wygenerowane ID
            val productId = db.productDao().insertProduct(productWithUser).toInt()
            val productWithId = product.copy(id = productId, userId = userId) // Utwórz kopię obiektu z przypisanym ID

            // Zapisz produkt w Firebase
            firebaseHelper.addProductToFirebase(productWithId)
        }else{
            Log.e("SaveProduct", "Something went wrong")
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

                var monthRepair = selectedMonth + 1 //months are 0-indexed, so we need add 1
                // Update TextView
                binding.editTextProductExpiryDate.setText("$selectedDay/$monthRepair/$selectedYear")
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}
