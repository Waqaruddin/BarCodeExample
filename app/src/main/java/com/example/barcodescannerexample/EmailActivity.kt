package com.example.barcodescannerexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_email.*

class EmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)

        initViews()
    }

    private fun initViews() {
        if (intent.getStringExtra("email_address") != null) {
            txtEmailAddress.text = "Recipient : " + intent.getStringExtra("email_address")

        }

        btnSendEmail.setOnClickListener {
            var intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL, txtEmailAddress.text.toString() as Array<String>)
            intent.putExtra(Intent.EXTRA_SUBJECT, inSubject.text.toString().trim())
            intent.putExtra(Intent.EXTRA_TEXT, inBody.text.toString().trim())

            startActivity(Intent.createChooser(intent, "Send Email"))
        }
    }
}