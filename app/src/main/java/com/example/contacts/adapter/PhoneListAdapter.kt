package com.example.contacts.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.contacts.R
import com.example.contacts.fragment.Call

class PhoneListAdapter(context: Context, private val phoneNoList: List<Long>, private val call: Call) : ArrayAdapter<Long>(context, 0, phoneNoList){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.phone_no_list, null, false)

        view.findViewById<TextView>(R.id.dt_phone).text = phoneNoList[position].toString()
        view.setOnClickListener {
            call.makeCallFromNo(position)
        }

        return view
    }

}