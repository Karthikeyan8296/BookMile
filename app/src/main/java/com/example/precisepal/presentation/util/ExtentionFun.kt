package com.example.precisepal.presentation.util

import kotlin.math.pow
import kotlin.math.roundToInt

fun Float.roundToDecimal(decimalPlace: Int = 1): Float{
    var multiplier = 10.0.pow(decimalPlace)
    return (this * multiplier).roundToInt() / multiplier.toFloat()
}

//  multiplier = 1000
//  7.562465 * 1000 = 7.562465
//  (7.562465).roundToInt() = 7562
//  7562/1000 = 7.562