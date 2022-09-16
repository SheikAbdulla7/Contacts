package com.example.contacts.viewmodels

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class FragmentLifecycleObserver() : DefaultLifecycleObserver {
    private lateinit var tag: String

    constructor(tag: String): this(){
        this.tag = tag
    }


    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        println(owner)
        Log.d(tag, "Created")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        println(owner)
        Log.d(tag, "Started")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        println(owner)
        Log.d(tag, "Resumed")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        println(owner)
        Log.d(tag, "Paused")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        println(owner)
        Log.d(tag, "Stopped")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        println(owner)
        Log.d(tag, "Destroyed")
    }
}