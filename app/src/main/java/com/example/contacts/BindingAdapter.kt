package com.example.contacts

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.entity.User

@BindingAdapter("android:visibility")
fun setVisibility(view: View, contacts: LiveData<List<User>>){
    val flag = contacts.value?.isNotEmpty()
    if (RecyclerView::class.java.isInstance(view)){
        view.visibility = if (flag == true) View.VISIBLE else View.GONE
    } else{
        view.visibility = if (flag == false) View.VISIBLE else View.GONE
    }
}