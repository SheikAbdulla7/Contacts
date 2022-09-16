package com.example.contacts.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.contacts.DatabaseHelper
import com.example.contacts.entity.User
import com.example.contacts.repositories.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ContactListViewModel @Inject constructor(private var repository: ContactsRepository) : ViewModel() {

//    private lateinit var repository: ContactsRepository
    lateinit var contactsList: LiveData<List<User>>
    private val _favourite: MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }
    val favourite: LiveData<Boolean> = _favourite

    private val _userDetail: MutableLiveData<User> by lazy {
        MutableLiveData()
    }
    val userDetail: LiveData<User> = _userDetail

//    private val mediator = MediatorLiveData<List<User>>()
//    private val _favouriteList:  MutableLiveData<List<User>> by lazy {
//        MutableLiveData()
//    }
//
//    val favouriteList : LiveData<List<User>> = _favouriteList

    init {
        contactsList = repository.getAllContacts()
    }


    fun initializeRepo(){
//        repository = ContactsRepository(DatabaseHelper.setDatabase(context).getUserDao())


        println("first favourite value : ${favourite.value}")

    }

    fun addOrUpdateContact(user: User, isAdding: Boolean = false){
        viewModelScope.launch (Dispatchers.IO){
            if (isAdding) {
                repository.addContact(user)
            } else {
                repository.updateUser(user)
            }

        }
    }

    fun updateFavourite(uid: Int, favourite: Boolean){
        _favourite.postValue(favourite)
        viewModelScope.launch (Dispatchers.IO){
            repository.updateFavourite(uid, favourite)
        }

    }

    fun deleteContact(){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteContact(userDetail.value)
        }
    }

    fun setSelectedDetails(user: User){
        _userDetail.postValue(user)
        _favourite.postValue(user.favourite)
    }



}