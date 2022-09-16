package com.example.contacts

import com.example.contacts.entity.User

interface FavouriteClickable {
    fun onFavouriteItemClicked()
    fun onFavouriteItemMenuClicked(user: User)
}