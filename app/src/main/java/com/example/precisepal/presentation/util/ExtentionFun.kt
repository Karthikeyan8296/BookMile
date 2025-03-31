package com.example.precisepal.presentation.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.pow
import kotlin.math.roundToInt

fun Float.roundToDecimal(decimalPlace: Int = 1): Float {
    val multiplier = 10.0.pow(decimalPlace)
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

//converting the millis dates to proper date string
@RequiresApi(Build.VERSION_CODES.O)
fun Long?.changeMillisToGraphDate(): LocalDate {
    return try {
        this?.let {
            Instant
                .ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        } ?: LocalDate.now()
    } catch (e: Exception) {
        LocalDate.now()
    }
}

//not allowing user to select the past dates
//we are getting that interface
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
object pastOrPresentSelectableDates : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis <= System.currentTimeMillis()
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year <= LocalDate.now().year
    }
}

//convert string to float
fun String.toFloatValue(decimalPlace: Int = 1): Float {
    val multiplier = 10.0.pow(decimalPlace)
    val value = this.toFloatOrNull() ?: 0f
    return (value * multiplier).roundToInt() / multiplier.toFloat()
}

fun String.toIntValue(): Int {
    return this.toFloatOrNull()?.roundToInt() ?: 0
}