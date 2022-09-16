package com.example.contacts.di

import android.content.Context
import com.example.contacts.DatabaseHelper
import com.example.contacts.dao.UserDao
import com.example.contacts.repositories.ContactsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideUserDao(@ApplicationContext context: Context) : UserDao {
        return DatabaseHelper.setDatabase(context).getUserDao()
    }


    @Provides
    @Singleton
    fun provideContactsRepository(userDao: UserDao) : ContactsRepository = ContactsRepository(userDao)

}