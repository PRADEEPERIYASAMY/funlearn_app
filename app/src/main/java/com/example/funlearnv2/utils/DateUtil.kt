package com.example.funlearnv2.utils

import java.text.SimpleDateFormat
import java.util.Date

fun Date.dateString(): String = SimpleDateFormat("dd/MM/yyyy").format(this)

fun Date.timeString(): String = SimpleDateFormat("HH:mm a").format(this)

fun Date.dateAndTimeString(): String = SimpleDateFormat("dd.MM.yyyy - h:mm a").format(this)
