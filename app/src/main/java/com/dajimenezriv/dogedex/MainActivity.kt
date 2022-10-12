package com.dajimenezriv.dogedex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dajimenezriv.dogedex.auth.LoginActivity
import com.dajimenezriv.dogedex.databinding.ActivityMainBinding
import com.dajimenezriv.dogedex.models.User
import com.dajimenezriv.dogedex.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = User.getLoggedInUser(this);
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        binding.settingsFab.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}