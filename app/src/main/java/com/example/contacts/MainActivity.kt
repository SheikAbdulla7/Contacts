package com.example.contacts

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.contacts.databinding.ActivityMainBinding
import com.example.contacts.fragment.SupportFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setTitle("adsfasa")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        application

//        setActionBarIcon(R.drawable.zd_baseline_arrow_back_24, getSupportActionBar(), title);
//        setActionBar(findViewById<Toolbar>(R.id.toolbar))

        if(supportActionBar != null){

        }
//        binding.toolbar.title = "Prakash"
//
//        setSupportActionBar(binding.toolbar)

        Log.d(">>>", "support action bar in activity ${supportActionBar?.title}")
        println("================================================")
        println(supportActionBar)
        lifecycleScope.launch(Dispatchers.IO){
            DatabaseHelper.setDatabase(applicationContext)

        }
        if(savedInstanceState == null){
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.fragment_container, SupportFragment())
//                add<ContactListFragment>(R.id.fragment_container, ContactListFragment())
            }



//            binding.tabLayout.setupWithViewPager(binding.pager)
//
//            val pagerAdapter = ViewPagerAdapter(supportFragmentManager)
//            pagerAdapter.addFragment(FavouritesFragment(), "Favourites")
//            pagerAdapter.addFragment(ContactListFragment(), "People")
//
//            binding.pager.adapter = pagerAdapter
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d("<<<", "onResume: $actionBar")
    }

    fun updateTitle() {
//        binding.toolbar.setTitle("Venkat")
    }


    override fun onDestroy() {
        super.onDestroy()
        println("Destroyed")
    }
}