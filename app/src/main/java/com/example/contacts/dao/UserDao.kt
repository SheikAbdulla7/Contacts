package com.example.contacts.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.contacts.entity.User

@Dao
interface UserDao {

    @Insert
    fun addContact(user: User)

    @Query("UPDATE users SET favourite = :favourite WHERE uid = :uid")
    fun updateContact(uid: Int, favourite: Boolean)

    @Update
    fun updateContact(user: User)

    @Delete
    fun deleteContact(user: User?)

    @Query("SELECT * FROM users")
    fun getAllContacts(): LiveData<List<User>>
}
