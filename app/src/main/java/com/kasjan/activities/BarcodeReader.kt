package com.kasjan.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Size
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kasjan.databinding.ActivityBarcodeReaderBinding
import com.kasjan.feauters.BarcodeAnalyzer

class BarcodeReader : AppCompatActivity() {

    private lateinit var binding: ActivityBarcodeReaderBinding
    private var barcodeResult: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarcodeReaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sprawdź uprawnienia do aparatu
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        } else {
            // Jeśli uprawnienia są już przyznane, uruchom kamerę
            startEanReader()
        }
    }

    // Metoda na obsługę wyniku skanowania
    private fun startEanReader() {
        // Sprawdź, czy już mamy wynik
        barcodeResult?.let {
            // Jeśli wynik jest już dostępny, uruchamiamy nową aktywność
            val intent = Intent(this, AddProductActivity::class.java)
            intent.putExtra("barcode", it)  // Przekazywanie wyniku do następnej aktywności
            startActivity(intent)
            return
        }

        // Zainicjalizuj kamerę
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Tworzymy obiekt Preview
            val preview = Preview.Builder()
                .setTargetResolution(Size(1280, 720)) // Możesz zmienić rozdzielczość
                .build()

            // Związanie Preview z PreviewView
            preview.setSurfaceProvider(binding.previewView.surfaceProvider)

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            // Skaner kodu kreskowego
            val analyzer = BarcodeAnalyzer { barcode ->
                runOnUiThread {
                    // Wstaw kod EAN do pola tekstowego
                    barcodeResult = barcode.rawValue

                    // Od razu uruchom nową aktywność po znalezieniu kodu kreskowego
                    barcodeResult?.let {

                        // Przekazanie EAN do nowej aktywności
                        val intent = Intent(this, AddProductActivity::class.java).apply {
                            putExtra("EAN", barcode.rawValue)
                        }
                        startActivity(intent)
                        finish()
                    }
                }
            }

            // Konfiguracja ImageAnalysis do analizy obrazu
            val analysisUseCase = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720)) // Ustaw rozdzielczość
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(ContextCompat.getMainExecutor(this), analyzer)
                }

            // Bindowanie kamery i wszystkich przypadków użycia do cyklu życia
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, analysisUseCase)
        }, ContextCompat.getMainExecutor(this))
    }

    // Obsługuje wynik zgody na uprawnienia kamery
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Jeśli użytkownik udzielił uprawnień, uruchom kamerę
                startEanReader()
            } else {
                // Jeśli brak uprawnień, informuj użytkownika
                // Możesz dodać obsługę przypadku braku uprawnień
            }
        }
    }
}
