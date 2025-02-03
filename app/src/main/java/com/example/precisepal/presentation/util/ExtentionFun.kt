package com.example.precisepal.presentation.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.pow
import kotlin.math.roundToInt

fun Float.roundToDecimal(decimalPlace: Int = 1): Float {
    var multiplier = 10.0.pow(decimalPlace)
    return (this * multiplier).roundToInt() / multiplier.toFloat()
}

//  multiplier = 1000
//  7.562465 * 1000 = 7.562465
//  (7.562465).roundToInt() = 7562
//  7562/1000 = 7.562

//converting the date to string
//like 12-7 -> July 12
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate?.changeLocalDateToGraphDate(defaultValue: LocalDate = LocalDate.now()): String {
    return try {
        this?.format(DateTimeFormatter.ofPattern("MMM dd"))
            ?: defaultValue.format(DateTimeFormatter.ofPattern("MMM dd"))
    } catch (e: Exception) {
        defaultValue.format(DateTimeFormatter.ofPattern("MMM dd"))
    }
}

//converting the date to string
//like 12-7-2024 -> 12 July 2024
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate?.changeLocalDateToFullDate(defaultValue: LocalDate = LocalDate.now()): String {
    return try {
        this?.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
            ?: defaultValue.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
    } catch (e: Exception) {
        defaultValue.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
    }
}