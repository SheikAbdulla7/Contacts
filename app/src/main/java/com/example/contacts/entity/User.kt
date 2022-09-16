package com.example.contacts.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "phone") var phoneNumber: List<Long>,
    @ColumnInfo(name = "email") var email: String? = null,
    @ColumnInfo(name = "photoUri") var photoUri: String? = null,
    @ColumnInfo(name = "favourite") var favourite: Boolean = false
)
