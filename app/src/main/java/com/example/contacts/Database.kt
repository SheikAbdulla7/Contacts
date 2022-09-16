package com.example.contacts

import android.content.Context
import androidx.room.*
import com.example.contacts.dao.UserDao
import com.example.contacts.entity.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [User::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUserDao() : UserDao
}


object DatabaseHelper{

    private lateinit var db: AppDatabase

    fun setDatabase(context: Context) : AppDatabase{
        println("Weeeeeeiiiiirrrrrddddd : ${::db}")
        if(!::db.isInitialized){
            db = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "Contact-Db"
            ).build()
            return db
        }
        return db


        println("Database configured : $db")
    }



}