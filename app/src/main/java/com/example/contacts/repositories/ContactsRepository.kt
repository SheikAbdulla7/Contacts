package com.example.contacts.repositories

import com.example.contacts.dao.UserDao
import com.example.contacts.entity.User
import dagger.hilt.EntryPoint
import javax.inject.Inject


class ContactsRepository @Inject constructor(private val userDao: UserDao) {
//    val contacts: LiveData<List<User>> = database.getUserDao().getAllContacts()

    fun getAllContacts() = userDao.getAllContacts()

    fun addContact(user: User){
        println("Inserted from Dao : ${userDao.addContact(user)}")
    }

    fun updateUser(user: User){
        userDao.updateContact(user)
    }

    fun updateFavourite(uid: Int, favourite: Boolean){
        userDao.updateContact(uid, favourite)
    }

    fun deleteContact(user: User?){
        userDao.deleteContact(user)
    }
}