package com.example.contacts

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream

object Utils{

    fun getAllSourceForImage(context: Context?) : Intent{
        val allIntents = mutableListOf<Intent>()
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        val galleryList: MutableList<ResolveInfo> =
            context?.packageManager?.queryIntentActivities(galleryIntent, 0) as MutableList<ResolveInfo>


        allIntents.addAll( galleryList.map { res ->
            val intent = Intent(galleryIntent)
            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.setPackage(res.activityInfo.packageName)
            intent
        })

        println(allIntents)

        var mainIntent: Intent? = allIntents.find { intent ->
            intent.component?.className?.equals("com.android.documentsui.DocumentsActivity") == true
        }


        if (mainIntent == null){
            mainIntent = allIntents[allIntents.size - 1]
        }
        println("Maaaaaaaiiiiiinnnnnnnn Intent : $mainIntent")
        allIntents.remove(mainIntent)

        val chooserIntent = Intent.createChooser(mainIntent, "Select source")

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toTypedArray())

        return chooserIntent

    }

    fun getAllSourceForCamera(context: Context?) : Intent{

        val allCameraIntents = mutableListOf<Intent>()

        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val cameraList = context?.packageManager?.queryIntentActivities(captureIntent, 0)

        cameraList?.let { cameraList ->
            allCameraIntents.addAll(cameraList.map { res ->
                val intent = Intent(captureIntent)
                intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
                intent.setPackage(res.activityInfo.packageName)
                intent
            })
        }

        println("Cameraaaaaaaaaaaa")
        println(allCameraIntents)
        allCameraIntents.forEach {
            println(it.component?.className)
        }

        var mainIntent: Intent? = allCameraIntents.find { intent ->
            intent.component?.className?.equals("com.android.documentsui.DocumentsActivity") == true
        }


        if (mainIntent == null){
            mainIntent = allCameraIntents[allCameraIntents.size - 1]
        }


//        allCameraIntents.remove(mainIntent)

        val chooseIntent = Intent.createChooser(mainIntent, "Select Camera source")

        chooseIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allCameraIntents.toTypedArray())
        return chooseIntent



    }

    internal fun writeImage(image: Bitmap?, imageFile: File){
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(imageFile)
            image?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        } catch (e: Exception){
            println(e.message)
        } finally {
            fos?.close()
        }
    }


    fun makePhoneCall(phoneNo: Long) : Intent{
        val callIntent = Intent().apply {
            action = Intent.ACTION_CALL
            data = Uri.parse("tel:${phoneNo}")
        }

        return callIntent
    }

}