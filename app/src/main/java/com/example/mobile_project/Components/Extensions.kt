package com.example.mobile_project.Components

fun Double.formatPrice(): String {
    return if (this % 1 == 0.0) {
        String.format("%.0f€", this)
    } else {
        String.format("%.2f€", this)
    }
}