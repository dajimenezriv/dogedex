package com.dajimenezriv.dogedex.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dajimenezriv.dogedex.main.MainActivity
import com.dajimenezriv.dogedex.databinding.ActivitySettingsBinding
import com.dajimenezriv.dogedex.models.User

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoutButton.setOnClickListener {
            User.destroyLoggedInUser(this)
            val intent = Intent(this, MainActivity::class.java)
            // all tasks below this activity are going to be deleted
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}