package com.example.contacts

import com.example.contacts.entity.User

interface Clickable {
    fun onContactItemClicked(user: User)
}