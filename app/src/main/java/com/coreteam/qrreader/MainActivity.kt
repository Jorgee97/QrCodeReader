package com.coreteam.qrreader

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val BARCODE_QR_READER_CODE = 505

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_scan_code.setOnClickListener {
            val requestImagenFromCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(requestImagenFromCameraIntent, BARCODE_QR_READER_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            BARCODE_QR_READER_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    qrCodeRecognoition(bitmap)
                }
            }
        }
    }

    private fun qrCodeRecognoition(picture: Bitmap) {
        val options = FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(
                FirebaseVisionBarcode.FORMAT_QR_CODE
            ).build()
        val image = FirebaseVisionImage.fromBitmap(picture)
        val detector = FirebaseVision.getInstance()
            .getVisionBarcodeDetector(options)

        detector.detectInImage(image)
            .addOnSuccessListener {
                if (it.size == 1) {
                    txt_information.text = ""
                    txt_information.text = it[0].rawValue
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "There was an error trying to read the QrCode.",
                    Toast.LENGTH_SHORT).show()
            }
    }
}
