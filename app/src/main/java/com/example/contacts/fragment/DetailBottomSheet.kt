package com.example.contacts.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.contacts.R
import com.example.contacts.Utils
import com.example.contacts.adapter.PhoneListAdapter
import com.example.contacts.databinding.FragmentBottomSheetBinding
import com.example.contacts.entity.User
import com.example.contacts.viewmodels.ContactListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.FieldPosition

class DetailBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetBinding
    private val viewModel: ContactListViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.userDetail.observe(this, Observer<User> { user ->
            bindDetails(user)
        })

        binding.dtPopupMenu.setOnClickListener { view ->
            showPopup(view)
        }

        binding.dtFavourite.setOnClickListener {
            val icon = viewModel.userDetail.value?.let { user ->
                println("kejfvjvfhgdgavhggduykgjZFYRYASEYTFJUGCYFFUYFJDCVJHDHFCSD")
                println(user.favourite)
                val newFavourite: Boolean
                println("first favourite value : ${viewModel.favourite.value}")
                val icon = if (viewModel.favourite.value == true){
                    newFavourite = false
                    context?.getDrawable(R.drawable.ic_unfavourite)

                } else{
                    newFavourite = true
                    context?.getDrawable(R.drawable.ic_favourite)
                }
                viewModel.updateFavourite(user.uid, newFavourite)

                icon
            }

            binding.dtFavourite.setImageDrawable(icon)

        }

        binding.dtEditContact.setOnClickListener {
            dismiss()

            setFragmentResult("navigate", bundleOf( "REQUEST" to "edit"))

        }

        binding.sendMail.setOnClickListener {
            val mailIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type="text/plain"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(viewModel.userDetail.value?.email))
                putExtra(Intent.EXTRA_SUBJECT, "Mail from Contacts")
                `package` = "com.google.android.gm"
            }
            startActivity(Intent.createChooser(mailIntent, "Send mail"))
        }
    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
//        if(requestCode == 1){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startActivity(viewModel.userDetail.value?.let { user ->
                    Utils.makePhoneCall(user.phoneNumber[requestCode])
                })
            } else {
              Toast.makeText(requireContext(), "Permission not granted", Toast.LENGTH_SHORT).show()
            }
//        }
    }

    private fun bindDetails(user: User){

        binding.dtProfileName.text = user.name

        val call = object : Call {
            override fun makeCallFromNo(position: Int) {
                if(ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf<String>(Manifest.permission.CALL_PHONE), position )
                } else{
                    startActivity(viewModel.userDetail.value?.let { user ->
                        Utils.makePhoneCall(user.phoneNumber[position])
                    })
                }
            }
        }

        binding.phoneNoList.adapter = PhoneListAdapter(requireContext(), user.phoneNumber, call)
//        binding.dtPhone.text = user.phoneNumber.toString()
        binding.dtEmail.text = user.email ?: "No email added"
        user.photoUri?.let {
            println(it.toUri())
            Glide.with(requireContext()).load(it.toUri()).into(binding.photoPicker)
            binding.dtProfileImage.visibility = View.GONE
        } ?: run {
            binding.dtProfileImage.text = user.name[0].uppercase()
            binding.contactProfile.visibility = View.GONE
        }
        binding.dtFavourite.setImageDrawable(context?.getDrawable(if (user.favourite) R.drawable.ic_favourite else R.drawable.ic_unfavourite))
    }

    private fun showPopup(view: View){
        val menuClickListener = PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete -> {
                    viewModel.deleteContact()
                    dismiss()
                    true
                }
                R.id.share -> {
                    shareContact()
                    true
                }
                else -> false
            }
        }
        PopupMenu(context, view).apply {
            setOnMenuItemClickListener(menuClickListener)
            inflate(R.menu.detail_popup_menu)
            show()
        }

    }

    private fun shareContact(){
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Mobile Number is ${viewModel.userDetail.value?.phoneNumber}")
            type = "text/plain"
        }

        startActivity(shareIntent)
    }
}

interface Call{
    fun makeCallFromNo(position: Int)
}