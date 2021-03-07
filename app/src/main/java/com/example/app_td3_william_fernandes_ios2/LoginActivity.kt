package com.example.app_td3_william_fernandes_ios2

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
        @JvmStatic
        external fun fetchPswd() : String // function from the C/C++ file to fetch the API's hashed URL
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val password = findViewById<EditText>(R.id.password)
        val but = findViewById<Button>(R.id.button)
        but.setOnClickListener {
            if(password.text.toString() == String(Base64.decode(LoginActivity.fetchPswd(), Base64.DEFAULT))) {
                val intent = Intent(this, DisplayDataActivity::class.java)
                startActivity(intent)
            }
            else {
                Toast.makeText(applicationContext, "Invalid password.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}