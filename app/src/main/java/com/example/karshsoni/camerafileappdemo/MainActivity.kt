package com.example.karshsoni.camerafileappdemo

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

    var mPhotoURI: Uri? = null

    lateinit var mPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissionCheck = ContextCompat.checkSelfPermission(this, "Manifest.permission.WRITE_EXTERNAL_STORAGE")
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 999)
        }

        btnCapture.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if(intent.resolveActivity(packageManager) != null){
                var photoFile: File? = null
                try{
                    photoFile = Utils.createImageFile()
                }catch (e: IOException){
                    e.printStackTrace()
                }
                if (photoFile != null){
                    mPhotoPath = photoFile.absolutePath
                    mPhotoURI = FileProvider.getUriForFile(this, "$packageName.fileprovider", photoFile)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoURI)
                    startActivityForResult(intent, 999)
                }
            }
        }

        imageView.setOnClickListener {
            var imageBitmap: Bitmap? = null

            if(mPhotoURI != null){
                try{
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, mPhotoURI)
                }catch (e: IOException){
                    e.printStackTrace()
                }
            }

            if(imageBitmap != null){
                imageView.setImageBitmap(Utils.getResizedBitmap(imageBitmap))
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            28 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "Permission Granted!!!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "Permission Denied!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 999 && resultCode == Activity.RESULT_OK){
            Toast.makeText(this, "Image Saved", Toast.LENGTH_LONG).show()
//            Utils.notifyGalleryAboutPic(this@MainActivity, mPhotoPath) -> To Notify Gallery
        }
    }

}
