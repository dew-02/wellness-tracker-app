package com.example.wellnesstracker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Always show onboarding, no SharedPreferences check
        setContentView(R.layout.activity_onboarding)

        val btnGetStarted = findViewById<Button>(R.id.btnGetStarted)
        btnGetStarted.setOnClickListener {
            // Go to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            // Optional: finish() if you want to remove onboarding from back stack
            finish()
        }
    }
}
