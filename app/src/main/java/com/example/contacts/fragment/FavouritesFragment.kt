package com.example.contacts.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.contacts.FavouriteClickable
import com.example.contacts.FindFragment
import com.example.contacts.Utils
import com.example.contacts.adapter.FavouriteListAdapter
import com.example.contacts.databinding.FragmentFavouritesBinding
import com.example.contacts.entity.User
import com.example.contacts.viewmodels.ContactDetailViewModel
import com.example.contacts.viewmodels.ContactListViewModel
import com.example.contacts.viewmodels.FragmentLifecycleObserver
import com.google.android.material.bottomsheet.BottomSheetBehavior


class FavouritesFragment : Fragment(), FindFragment {


    private lateinit var adapter: FavouriteListAdapter

    private lateinit var binding: FragmentFavouritesBinding

//    private lateinit var favouriteListAdapter: FavouriteListAdapter
    private val viewModel: ContactListViewModel by activityViewModels ()
    private val detailViewModel: ContactDetailViewModel by viewModels()
    private var favouriteListAdapter: FavouriteListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState == null){
//            detailViewModel.initializeRepo(requireContext())
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(FragmentLifecycleObserver("Lifecycle1"))
        val layoutManager = GridLayoutManager(context, 2)
        binding.favouriteRecyclerView.layoutManager = layoutManager



//        val bottomSheetLayout = binding.sheet
//        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
//        bottomSheetBehavior

//        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                bottomSheetBehavior.peekHeight = 0
//                bottomSheetLayout.visibility = View.VISIBLE
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//
//            }
//
//        })

        val itemClickListener = object : FavouriteClickable {

            override fun onFavouriteItemClicked() {
                if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf<String>(Manifest.permission.CALL_PHONE), 1 )
                } else{
                    try {
                        val intent = viewModel.userDetail.value?.let { user ->
                            Utils.makePhoneCall(user.phoneNumber[0])
                        }
                        startActivity(intent)
                    } catch (exception: Exception){
                        println(exception.message)
                    }
                }
            }

            override fun onFavouriteItemMenuClicked(user: User) {

//                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
////                    btn_bottom_sheet.setText("Close sheet")
//                } else {
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
////                    btn_bottom_sheet.setText("Expand sheet")
//                }

                viewModel.setSelectedDetails(user)
//                detailViewModel.setSelectedDetails(user)
                DetailBottomSheet().show(parentFragmentManager, "GET_USER")
            }
        }

        adapter = FavouriteListAdapter(requireContext(), itemClickListener)

        binding.favouriteRecyclerView.adapter = adapter


        viewModel.contactsList.observe(viewLifecycleOwner, Observer<List<User>> { contactList ->
            val favList = contactList.filter { user ->
                user.favourite
            }

            adapter.submitList(favList)
            adapter.filteringList = favList
//            binding.favouriteRecyclerView.adapter.

        })


    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 1){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startActivity(viewModel.userDetail.value?.let { user ->
                    Utils.makePhoneCall(user.phoneNumber[0])
                })
            } else {
                Toast.makeText(requireContext(), "Permission not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun filter(newText: String?) {
        adapter.getMyFilter().filter(newText)
    }

    override fun onResume() {
        super.onResume()
        SupportFragment.currentPage = this
    }

    override fun onPause() {
        super.onPause()
        SupportFragment.currentPage = null
    }

//     object FilterObject {
//        fun filter(newText: String?) {
//            adapter.getMyFilter().filter(newText)
//        }
//    }
//    }


}