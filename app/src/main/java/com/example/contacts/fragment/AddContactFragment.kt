package com.example.contacts.fragment

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.contacts.MainActivity
import com.example.contacts.R
import com.example.contacts.Utils
import com.example.contacts.databinding.FragmentAddContactBinding
import com.example.contacts.entity.User
import com.example.contacts.viewmodels.ContactListViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class AddContactFragment : Fragment() {

    private lateinit var binding: FragmentAddContactBinding
    private val viewModel: ContactListViewModel by activityViewModels ()

    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState == null){
        }

        Log.d("<<<", "${(activity as AppCompatActivity).supportActionBar?.title}")

        Log.d("<<<", "onCreate: ${(activity as AppCompatActivity).supportActionBar}")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddContactBinding.inflate(inflater, container, false)
        binding.addIncluded.btnAddContact.text = "Add"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).updateTitle()
        (activity as AppCompatActivity).supportActionBar?.title = "Add Contacts"

        binding.addIncluded.btnAddContact.setOnClickListener {
            val name = binding.addIncluded.contactName.text.toString()
            val phoneNoList = mutableListOf<Long>()
            for(index in 0 until binding.addIncluded.phoneNoMainLayout.childCount){
                println(binding.addIncluded.phoneNoMainLayout[index])
                println(binding.addIncluded.phoneNoMainLayout[index].findViewById<EditText>(R.id.contact_phone_no).text.toString())
                phoneNoList.add(binding.addIncluded.phoneNoMainLayout[index].findViewById<EditText>(R.id.contact_phone_no).text.toString().toLong())
            }
//            for(binding.addIncluded.phoneNoMainLayout.childCount
            val phone = binding.addIncluded.contactPhoneNo.text.toString().toLong()
            val email = binding.addIncluded.contactEmail.text.toString()
            val user = User(name = name, phoneNumber = phoneNoList, email = email, photoUri = photoUri?.toString())


            viewModel.addOrUpdateContact(user, true)

            Toast.makeText(context, "Contact Added Successfully", Toast.LENGTH_SHORT).show()
            activity?.supportFragmentManager?.popBackStack()
        }

        binding.addIncluded.addInputField.setOnClickListener {
            putLayout()
        }

        binding.addIncluded.photoPicker.setOnClickListener {
            resultLauncher.launch(Utils.getAllSourceForImage(context))
        }

        binding.addIncluded.cameraPicker.setOnClickListener{
            resultLauncher.launch(Utils.getAllSourceForCamera(context))
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }


    }



    private fun putLayout(){
        val view = layoutInflater.inflate(R.layout.secondary_phone_input, null, false)

        view.findViewById<ImageView>(R.id.remove_input_field).setOnClickListener { view ->
            binding.addIncluded.phoneNoMainLayout.removeView(view.parent as View)
        }

        binding.addIncluded.phoneNoMainLayout.addView(view)

    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            val imagesFolder = File(context?.getExternalFilesDir("/"), "ContactsMedia")
            println(imagesFolder.mkdirs())

            println("intent data : ${result.data}")
            println(result.data?.extras)
            val path = result.data

            Log.d("<<<", "data: ${result.data}")
            Log.d("<<<", "extra : ${result.data?.extras?.get("data")}")



//            println(data.extras?.get("data"))

            println(path)
            println(photoUri)


            val bitmap: Bitmap?
            val imageFile = if(result.data?.data != null) {
                val path = result.data?.data
                bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, path)
                File(imagesFolder, "${path.toString().split('/').last()}.jpg")
            } else{
                bitmap = result.data?.extras?.get("data") as Bitmap?
                println(bitmap)
                val date = SimpleDateFormat("yyMMdd_HHmmss").format(Date())

                File(imagesFolder, "captured_$date.jpg")
            }

            println("--------------------------------- $bitmap")

            photoUri = Uri.fromFile(imageFile)

            println(photoUri)

            Utils.writeImage(bitmap, imageFile)
            Glide.with(requireContext()).load(imageFile).into(binding.addIncluded.photoPicker)

        }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        if(data != null && requestCode == 1 && resultCode == Activity.RESULT_OK){


            val imagesFolder = File(context?.getExternalFilesDir("/"), "ContactsMedia")
            println(imagesFolder.mkdirs())

            println("intent data : ${data.data}")
            val path = data.data

            println(data.extras?.get("data"))

            println(path)
            println(photoUri)


            val bitmap: Bitmap?
            val imageFile = if(data.data != null) {
                val path = data.data
                bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, path)
                File(imagesFolder, "${path.toString().split('/').last()}.jpg")
            } else{
                bitmap = data.extras?.get("data") as? Bitmap
                val date = SimpleDateFormat("yyMMdd_HHmmss").format(Date())

                File(imagesFolder, "captured_$date.jpg")
            }


            photoUri = Uri.fromFile(imageFile)

            println(photoUri)

            Utils.writeImage(bitmap, imageFile)
            Glide.with(requireContext()).load(imageFile).into(binding.addIncluded.photoPicker)

        }
    }

}