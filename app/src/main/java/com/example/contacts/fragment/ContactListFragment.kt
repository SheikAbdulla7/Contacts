package com.example.contacts.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import com.example.contacts.Clickable
import com.example.contacts.FindFragment
import com.example.contacts.R
import com.example.contacts.adapter.ContactListAdapter
import com.example.contacts.databinding.FragmentContactListBinding
import com.example.contacts.entity.User
import com.example.contacts.viewmodels.ContactListViewModel
import com.example.contacts.viewmodels.FragmentLifecycleObserver
import java.io.File


class ContactListFragment : Fragment(), FindFragment {

    private lateinit var binding: FragmentContactListBinding
    private val viewModel: ContactListViewModel by activityViewModels ()
    private lateinit var adapter: ContactListAdapter
//    private lateinit var contactListAdapter: ContactListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("12345678900987654321234567890987654321`")
        println(context)

//        if(savedInstanceState == null){
//        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_contact_list, container, false)
        binding = FragmentContactListBinding.bind(rootView)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        activity?.title = "Contacts"

        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(FragmentLifecycleObserver("Lifecycle2"))

        println("fiiiiiillllllllllllleeeeeeeeeeeessssssssss")
        println(File(context?.getExternalFilesDir("/"), "ContactsMedia").listFiles()?.size)


        adapter = ContactListAdapter(requireContext()) { user ->
            viewModel.setSelectedDetails(user)
            DetailBottomSheet().show(parentFragmentManager, "GET_USER")
        }


        binding.contactList.adapter = adapter


        viewModel.contactsList.observe(viewLifecycleOwner) { contacts ->
            println(contacts)
            adapter.submitList(contacts)
            adapter.filteringList = contacts

        }
//        binding.contactList.adapter = ContactListAdapter(contactList ?: MutableLiveData<List<User>>())
        binding.addContact.setOnClickListener{
            println(childFragmentManager.findFragmentById(R.id.fragment_container))
            println(parentFragmentManager.findFragmentById(R.id.fragment_container))
            println(parentFragment)
            setFragmentResult("navigate", bundleOf( "REQUEST" to "add"))
        }



    }

    override fun onResume() {
        super.onResume()
        SupportFragment.currentPage = this
    }

    override fun onPause() {
        super.onPause()
        SupportFragment.currentPage = null
    }


    override fun filter(newText: String?) {
        adapter.getMyFilter().filter(newText)
    }



}
