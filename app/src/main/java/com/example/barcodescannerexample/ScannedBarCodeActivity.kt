package com.example.barcodescannerexample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceHolder.Callback
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Processor
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_scanned_bar_code.*
import java.io.IOException


class ScannedBarCodeActivity : AppCompatActivity() {
    lateinit var barcodeDetector: BarcodeDetector
    lateinit var cameraSource: CameraSource
    private val REQUEST_CAMERA_PERMISSION = 201
    var isEmail = false
    var intentData = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanned_bar_code)

        initViews()
    }

    private fun initViews() {
        btnAction.setOnClickListener {
            if (intentData.isNotEmpty()) {
                if (isEmail) {
                    startActivity(
                        Intent(this, EmailActivity::class.java).putExtra(
                            "email_address",
                            intentData
                        )
                    )
                } else {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(intentData)))
                }
            }
        }
    }

    private fun initializeDetectorsAndSources() {
        Toast.makeText(this, "Barcode scanner started", Toast.LENGTH_SHORT).show()
        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true)
            .build()

        surfaceView.holder.addCallback(object : Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {

                try {
                    if (ActivityCompat.checkSelfPermission(
                            this@ScannedBarCodeActivity,
                            android.Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        cameraSource.start(surfaceView.holder)
                    } else {
                        ActivityCompat.requestPermissions(
                            this@ScannedBarCodeActivity,
                            arrayOf(Manifest.permission.CAMERA),
                            REQUEST_CAMERA_PERMISSION
                        )
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }

        })

        barcodeDetector.setProcessor(object : Processor<Barcode> {
            override fun release() {
                Toast.makeText(
                    applicationContext,
                    "To prevent memory leaks barcode scanner has been stopped",
                    Toast.LENGTH_SHORT
                ).show();
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
                val barcodes = detections!!.detectedItems

                if(barcodes.size() !=0){
                    txtBarcodeValue.post(object: Runnable{
                        override fun run() {
                            if(barcodes.valueAt(0).email != null){
                                txtBarcodeValue.removeCallbacks(null)
                                intentData = barcodes.valueAt(0).email.address
                                txtBarcodeValue.text = intentData
                                isEmail = true
                                btnAction.text = "Add Content to the Email"
                            }else{
                                isEmail = false
                                btnAction.text = "Launch Url"
                                intentData = barcodes.valueAt(0).displayValue
                                txtBarcodeValue.text = intentData
                            }
                        }

                    })
                }

            }

        })
    }


    override fun onPause() {
        super.onPause()
        cameraSource.release()
    }

    override fun onResume() {
        super.onResume()
        initializeDetectorsAndSources()
    }

}