package com.example.barcodescannerexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnScanBarcode.setOnClickListener {
            startActivity(Intent(this, ScannedBarCodeActivity::class.java ))
        }
    }
}