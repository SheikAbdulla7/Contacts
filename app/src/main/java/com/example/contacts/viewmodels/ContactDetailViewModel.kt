package com.example.contacts.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contacts.DatabaseHelper
import com.example.contacts.entity.User
import com.example.contacts.repositories.ContactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactDetailViewModel : ViewModel() {

    private lateinit var repository: ContactsRepository

    fun initializeRepo(context: Context){
        repository = ContactsRepository(DatabaseHelper.setDatabase(context).getUserDao())
    }

    private val _userDetail: MutableLiveData<User> by lazy {
        MutableLiveData()
    }
    val userDetail: LiveData<User> = _userDetail

    private val _favourite: MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }
    val favourite: LiveData<Boolean> = _favourite


    fun updateFavourite(uid: Int, favourite: Boolean){
        _favourite.postValue(favourite)
        viewModelScope.launch (Dispatchers.IO){
            repository.updateFavourite(uid, favourite)
        }

    }

    fun setSelectedDetails(user: User){
        _userDetail.postValue(user)
        _favourite.postValue(user.favourite)
    }
}