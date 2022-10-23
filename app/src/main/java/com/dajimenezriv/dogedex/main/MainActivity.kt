package com.dajimenezriv.dogedex.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.dajimenezriv.dogedex.LABELS_PATH
import com.dajimenezriv.dogedex.MODEL_PATH
import com.dajimenezriv.dogedex.R
import com.dajimenezriv.dogedex.WholePictureActivity
import com.dajimenezriv.dogedex.WholePictureActivity.Companion.PICTURE_URI_KEY
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.api.APIServiceInterceptor
import com.dajimenezriv.dogedex.auth.LoginActivity
import com.dajimenezriv.dogedex.databinding.ActivityMainBinding
import com.dajimenezriv.dogedex.dogdetail.DogDetailActivity
import com.dajimenezriv.dogedex.dogdetail.DogDetailActivity.Companion.DOG_KEY
import com.dajimenezriv.dogedex.doglist.DogListActivity
import com.dajimenezriv.dogedex.machinelearning.Classifier
import com.dajimenezriv.dogedex.models.User
import com.dajimenezriv.dogedex.settings.SettingsActivity
import org.tensorflow.lite.support.common.FileUtil
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var classifier: Classifier
    private var isCameraReady = false
    private val viewModel: MainViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // permission is granted
                setupCamera()
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

        val user = User.getLoggedInUser(this)
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        APIServiceInterceptor.setSessionToken(user.authenticationToken)

        binding.dogListFab.setOnClickListener {
            startActivity(Intent(this, DogListActivity::class.java))
        }

        binding.settingsFab.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        viewModel.status.observe(this) { status ->
            when (status) {
                is APIResponseStatus.Error -> {
                    binding.loadingWheel.visibility = View.GONE
                    Toast.makeText(this, status.messageId, Toast.LENGTH_SHORT).show()
                }
                is APIResponseStatus.Loading -> {
                    // show progress bar
                    binding.loadingWheel.visibility = View.VISIBLE
                }
                is APIResponseStatus.Success -> {
                    // hide progress bar
                    binding.loadingWheel.visibility = View.GONE
                }
            }
        }

        viewModel.dog.observe(this) { dog ->
            if (dog != null) {
                val intent = Intent(this, DogDetailActivity::class.java)
                intent.putExtra(DOG_KEY, dog)
                startActivity(intent)
            }
        }

        requestCameraPermissions()
    }

    override fun onStart() {
        super.onStart()
        classifier = Classifier(
            FileUtil.loadMappedFile(this@MainActivity, MODEL_PATH),
            FileUtil.loadLabels(this@MainActivity, LABELS_PATH)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::cameraExecutor.isInitialized) cameraExecutor.shutdown()
    }

    private fun requestCameraPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // we can use the api that requires the permission
                    setupCamera()
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
            setupCamera()
        }
    }

    private fun setupCamera() {
        // we will wait until the cameraPreview is initialized
        // otherwise, it will throw a null pointer exception when trying to access to display
        binding.cameraPreview.post {
            imageCapture = ImageCapture.Builder()
                .setTargetRotation(binding.cameraPreview.display.rotation)
                .build()
            cameraExecutor = Executors.newSingleThreadExecutor()
            startCamera()
            isCameraReady = true
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)

            // machine learning
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                val bitmap = convertImageProxyToBitmap(imageProxy)
                if (bitmap != null) {
                    val dogRecognition = classifier.recognizeImage(bitmap).first()
                    // enable camera button
                    if (dogRecognition.confidence > 70) {
                        binding.takePhotoFab.alpha = 1f
                        binding.takePhotoFab.setOnClickListener {
                            viewModel.getDogByMlId(dogRecognition.id)
                        }
                    } else {
                        binding.takePhotoFab.alpha = 0.2f
                        binding.takePhotoFab.setOnClickListener(null)
                    }
                }

                imageProxy.close()
            }

            // camera to lifecycle
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageCapture,
                imageAnalysis
            )
        }, ContextCompat.getMainExecutor(this))
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun convertImageProxyToBitmap(imageProxy: ImageProxy): Bitmap? {
        val image = imageProxy.image ?: return null

        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 100, out)
        val imageBytes = out.toByteArray()

        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    private fun takePicture() {
        if (!isCameraReady) return

        val outputFileOptions =
            ImageCapture.OutputFileOptions.Builder(getOutputPictureFile()).build()
        imageCapture.takePicture(
            // a cameraExecutor is just a thread to execute the camera there
            outputFileOptions, cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException) {
                    Toast.makeText(
                        this@MainActivity,
                        "Error taking picture: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val uri = outputFileResults.savedUri

                    // openPicture(uri.toString())
                }
            }
        )
    }

    private fun getOutputPictureFile(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name) + ".jpg").apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    private fun openPicture(pictureUri: String) {
        val intent = Intent(this, WholePictureActivity::class.java)
        intent.putExtra(PICTURE_URI_KEY, pictureUri)
        startActivity(intent)
    }
}