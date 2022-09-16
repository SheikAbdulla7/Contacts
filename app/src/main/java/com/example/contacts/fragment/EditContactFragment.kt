package com.example.contacts.fragment

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.contacts.R
import com.example.contacts.Utils
import com.example.contacts.databinding.FragmentEditContactBinding
import com.example.contacts.entity.User
import com.example.contacts.viewmodels.ContactListViewModel
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class EditContactFragment : Fragment() {

    private lateinit var binding: FragmentEditContactBinding
    private val viewModel: ContactListViewModel by activityViewModels()
    private var photoUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditContactBinding.inflate(inflater, container, false)
        binding.editIncluded.btnAddContact.text = "Update"

        activity?.title = "Add new Contact"
        activity?.actionBar?.setDisplayShowHomeEnabled(true)

        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        binding.btnBack.setOnClickListener { onBackPressed() }

        binding.editIncluded.contactName.setText(viewModel.userDetail.value?.name.toString())
        binding.editIncluded.contactPhoneNo.setText(viewModel.userDetail.value?.phoneNumber?.get(0).toString())

        viewModel.userDetail.value?.phoneNumber?.forEachIndexed { index, value ->
            if(index != 0){
                val view = layoutInflater.inflate(R.layout.secondary_phone_input, null, false)

                view.findViewById<ImageView>(R.id.remove_input_field).setOnClickListener { view ->
                    binding.editIncluded.phoneNoMainLayout.removeView(view.parent as View)
                }
                view.findViewById<TextInputEditText>(R.id.contact_phone_no).setText(value.toString())
                binding.editIncluded.phoneNoMainLayout.addView(view)
            }


        }

//        binding.editIncluded.contactPhoneNo.setText(viewModel.userDetail.value?.phoneNumber.toString())
        binding.editIncluded.contactEmail.setText(viewModel.userDetail.value?.email ?: "")
        photoUri = viewModel.userDetail.value?.photoUri?.let {
            Glide.with(requireContext()).load(it).into(binding.editIncluded.photoPicker)
            it.toUri()
        }





        binding.editIncluded.addInputField.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.secondary_phone_input, null, false)



            view.findViewById<ImageView>(R.id.remove_input_field).setOnClickListener { view ->
                binding.editIncluded.phoneNoMainLayout.removeView(view.parent as View)
            }

            binding.editIncluded.phoneNoMainLayout.addView(view)
        }



        binding.editIncluded.photoPicker.setOnClickListener {
            resultLauncher.launch(Utils.getAllSourceForImage(context))
        }

        binding.editIncluded.cameraPicker.setOnClickListener{
            resultLauncher.launch(Utils.getAllSourceForCamera(context))
        }

        binding.editIncluded.btnAddContact.setOnClickListener {

            val name = binding.editIncluded.contactName.text.toString()

            val phoneNoList = mutableListOf<Long>()
            for(index in 0 until binding.editIncluded.phoneNoMainLayout.childCount){
                println(binding.editIncluded.phoneNoMainLayout[index])
                println(binding.editIncluded.phoneNoMainLayout[index].findViewById<EditText>(R.id.contact_phone_no).text.toString())
                phoneNoList.add(binding.editIncluded.phoneNoMainLayout[index].findViewById<EditText>(
                    R.id.contact_phone_no).text.toString().toLong())
            }
            val email = binding.editIncluded.contactEmail.text.toString()
            println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ ${photoUri?.toString() is String}")

            val editUser = viewModel.userDetail.value as User
            editUser.apply {
                uid = viewModel.userDetail.value?.uid!!
                this.name = name
                phoneNumber = phoneNoList
                this.email = email
                this.photoUri = this@EditContactFragment.photoUri?.toString()
                favourite = viewModel.userDetail.value?.favourite == true

            }

            viewModel.addOrUpdateContact(editUser)

            Toast.makeText(context, "Contact Updated Successfully", Toast.LENGTH_SHORT).show()
            activity?.supportFragmentManager?.popBackStack()
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

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
            Glide.with(requireContext()).load(imageFile).into(binding.editIncluded.photoPicker)

        }

    }


}