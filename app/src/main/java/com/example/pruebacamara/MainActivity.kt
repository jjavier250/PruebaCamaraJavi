package com.example.pruebacamara

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import android.Manifest
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import android.widget.TextView
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {

        lateinit var btnOpenCamera:Button
        lateinit var imageView:ImageView
        lateinit var txtbit:TextView
        private val REQUEST_CODE_CAMERA = 100
        private val REQUEST_CODE_PERMISSIONS = 101

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            btnOpenCamera=findViewById(R.id.btnOpenCamera)
            imageView=findViewById(R.id.imageView)
            txtbit=findViewById(R.id.txtbit)

            btnOpenCamera.setOnClickListener {
                if (checkPermissions()) {
                    openCamera()

                    val imageBitmap = (imageView.drawable as BitmapDrawable).bitmap
                    val imageBytes = sacarBitmap(imageBitmap)
                   // txtbit.text=imageBytes.toString()

                } else {
                    requestPermissions()
                }
            }
        }

        private fun checkPermissions(): Boolean {
            return (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED)
        }

        private fun requestPermissions() {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CODE_PERMISSIONS)
        }

        private fun openCamera() {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_CODE_CAMERA)
        }

        override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == REQUEST_CODE_PERMISSIONS) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }

    private fun sacarBitmap(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    }
