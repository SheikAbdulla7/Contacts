package com.example.contacts

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromString(stringListString: String): List<Long> {
        return stringListString.split(",").map { it.toLong() }
    }

    @TypeConverter
    fun toString(stringList: List<Long>): String {
        return stringList.joinToString(separator = ",")
    }

}