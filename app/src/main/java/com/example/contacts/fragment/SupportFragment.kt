package com.example.contacts.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import com.example.contacts.FindFragment
import com.example.contacts.MainActivity
import com.example.contacts.R
import com.example.contacts.adapter.ViewPagerAdapter
import com.example.contacts.databinding.FragmentSupportBinding
import com.example.contacts.viewmodels.FragmentLifecycleObserver
import com.google.android.material.tabs.TabLayoutMediator


class SupportFragment : Fragment() {

    private lateinit var binding: FragmentSupportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    companion object{
        var currentPage : FindFragment? = null
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSupportBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val fragmentTitleList = mutableListOf( "Favourites", "People")


        activity?.actionBar?.let {
            it.title = "App"
        }

        (activity as MainActivity).updateTitle()
        (activity as MainActivity).setTitle("Contact")

        ((activity as MainActivity).actionBar as? Toolbar)?.let { actionBar ->
            actionBar.title = "venkat"
            (activity as MainActivity).setActionBar(actionBar)
        }
//
        Log.d(">>>", "support action bar in support fragment: ${(activity as MainActivity).supportActionBar?.title}")


        val searchListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                currentPage?.filter(newText)
                return false;
            }
        }
        binding.toolbar.apply {
            inflateMenu(R.menu.appbar_option_menu)
            menu.children.forEach { menuItem ->
                menuItem.icon.setTint(resources.getColor(R.color.white))
                val searchView = menuItem.actionView as? SearchView
                searchView?.apply {
                    queryHint = "Search Contacts"
                    setOnQueryTextListener(searchListener)

                }

            }
        }
//        binding.toolbar.menu.inflateMenu(R.menu.appbar_option_menu).apply {
//
//        }
//        binding.toolbar.menu.children.forEach { menuItem ->
//            menuItem.icon.setTint(resources.getColor(R.color.white))
//        }
        binding.toolbar.setOnMenuItemClickListener { menu ->
            when(menu.itemId){
                R.id.search -> {
                    menu.icon.setTint(resources.getColor(R.color.white))
                    Toast.makeText(context, "Search Clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.about -> {
                    menu.icon.setTint(resources.getColor(R.color.white))
                    parentFragmentManager.commit {
                        replace(R.id.fragment_container, AboutFragment())
                        addToBackStack(null)
                    }
                    true
                }
                else -> false
            }

        }
        println("calllllllllleeeeeeeddddddd")
        lifecycle.addObserver(FragmentLifecycleObserver("Lifecycle"))

        childFragmentManager.setFragmentResultListener("navigate", viewLifecycleOwner) { requestKey, bundle ->

            when{
                bundle.getString("REQUEST") == "add" -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, AddContactFragment())
                        .addToBackStack("AddContactFragment")
                        .commit()
                }

                bundle.getString("REQUEST") == "edit" -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, EditContactFragment())
                        .addToBackStack("AddContactFragment")
                        .commit()
                }
            }

        }

        val pagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle)
//        pagerAdapter.
        pagerAdapter.addFragment(FavouritesFragment(), "Favourites")
        pagerAdapter.addFragment(ContactListFragment(), "People")

        binding.pager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.pager){ tab, position ->
            tab.text = fragmentTitleList[position]
        }.attach()
    }



}