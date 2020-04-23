package com.wcp.pickmutipleimage

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private lateinit var imageAdapter: ImageAdapter

    private var imagePath: Uri? = null

    companion object {
        private const val MY_REQUEST_CODE = 222
        private const val REQUEST_CAMERA = 103
        private const val CAMERA_REQUEST = 102
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setAdapter()

        btnGallery.setOnClickListener {
            selectUserImageFromGallery()
        }

        btnCamera.setOnClickListener {
            selectUserImageFromCamera()
        }
    }

    private fun setAdapter() {
        imageAdapter = ImageAdapter(this@MainActivity)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = imageAdapter
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(
                    Intent.createChooser(intent, "Choose Profile Image"),
                    MY_REQUEST_CODE
                )
            }
        }
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                strictModeOn()
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val file = File(
                    externalCacheDir,
                    System.currentTimeMillis().toString() + ".jpg"
                )
                imagePath = Uri.fromFile(file)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imagePath)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }
        }


    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            imagePath = data.data
            var resource: Bitmap =MediaStore.Images.Media.getBitmap(this.contentResolver, imagePath)
            imagePath = CompressUtil.compressImageUri(this, resource)
            imageAdapter.setImageUriList(imagePath!!)
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            var resource: Bitmap =
                MediaStore.Images.Media.getBitmap(this.contentResolver, imagePath)
            imagePath = CompressUtil.compressImageUri(this, resource)
            imageAdapter.setImageUriList(imagePath!!)
        }

    }

    private fun selectUserImageFromGallery() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            MY_REQUEST_CODE
        )

    }

    private fun selectUserImageFromCamera() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA),
            REQUEST_CAMERA
        )
    }

    private fun strictModeOn() {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }
}
