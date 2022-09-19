package com.example.contacts

import com.example.contacts.entity.User

fun interface Clickable {
    fun onContactItemClicked(user: User)
}