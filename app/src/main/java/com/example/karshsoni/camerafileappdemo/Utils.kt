package com.example.karshsoni.camerafileappdemo

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.Toast
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Utils{

    companion object {

        var imageFile : File? = null

        fun notifyGalleryAboutPic(context: Context, imagePath: String){
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            var file = File(imagePath)
            var contentUri = Uri.fromFile(file)
            intent.data = contentUri
            context.sendBroadcast(intent)
        }

        fun createImageFile(): File? {

            // .nomedia folder will not let any other android specific app to show our media files in their apps

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH)
                    .format(Date())

            val imageFileName = "IMG_$timeStamp"

            val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            val folderCreate = File(storageDir, ".nomedia")

            if(folderCreate.mkdir()){
                Log.d("Folder", "createImageFile: Folder Created")
            }

            imageFile = File(folderCreate,
                    "$imageFileName.jpg")

            var size = (storageDir.freeSpace.toDouble() * 0.000001)

            if (size < 100) {
                return null
            } else {
                return imageFile as File
            }

        }

        fun getResizedBitmap(originalBitmap: Bitmap): Bitmap{
            var width = originalBitmap.width
            var height = originalBitmap.height

            val maxSize = 960

            if(width > maxSize || height > maxSize){

                if(width > height){
                    val ratio = width / maxSize.toFloat()
                    width = maxSize
                    height = (height / ratio).toInt()
                }else if(height > width){
                    val ratio = height / maxSize.toFloat()
                    height = maxSize
                    width = (width / ratio).toInt()
                }else{
                    height = maxSize
                    width = maxSize
                }

            }

            val bitmapScaled = Bitmap.createScaledBitmap(originalBitmap, width, height,false)

            return bitmapScaled

        }

    }

}