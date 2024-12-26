package com.example.mobile_project.Home

import android.icu.text.SimpleDateFormat
import java.net.URLEncoder
import java.util.Date
import java.util.Locale

fun String.parseDate(): Date? {
    //2024-10-14T08:20:01Z
    val pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    return SimpleDateFormat(pattern, Locale.getDefault()).parse(this)
    //return date?.let {Date(it.year -1900,it.month,it.date)}
}

fun String.encodeURL(): String {
    return URLEncoder.encode(this, "UTF-8")
}

fun Date.NormalDate(): String {
    val pattern = "dd-MM-yyyy"
    return SimpleDateFormat(pattern, Locale.getDefault()).format(this)
}