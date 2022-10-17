package com.dajimenezriv.dogedex

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.dajimenezriv.dogedex.api.APIServiceInterceptor
import com.dajimenezriv.dogedex.auth.LoginActivity
import com.dajimenezriv.dogedex.databinding.ActivityMainBinding
import com.dajimenezriv.dogedex.doglist.DogListActivity
import com.dajimenezriv.dogedex.models.User
import com.dajimenezriv.dogedex.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // permission is granted
            } else {
                // explain to the user that the feature is unavailable
                Toast.makeText(
                    this,
                    getString(R.string.error_camera_permission),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = User.getLoggedInUser(this);
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        APIServiceInterceptor.setSessionToken(user.authenticationToken)

        binding.settingsFab.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.dogListFab.setOnClickListener {
            startActivity(Intent(this, DogListActivity::class.java))
        }

        // request permission of camera
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // we can use the api that requires the permission
                    startCamera()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
                -> {
                    // explain why the user needs the permission
                    // this appears in case that the user has cancel the permission before
                    AlertDialog.Builder(this)
                        .setTitle("Camera Permission")
                        .setMessage("Accept Camera Permission to track dogs.")
                        .setPositiveButton(android.R.string.ok) { _, _ ->
                            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                        .setNegativeButton(android.R.string.cancel) { _, _ -> }
                        .show()
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
        } else {
            // open camera, we don't need the permission
            startCamera()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraProvider.bindToLifecycle(this, cameraSelector, preview)
        }, ContextCompat.getMainExecutor(this))
    }
}