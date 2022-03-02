package com.test.pokemon.utility

import java.text.DecimalFormat
import kotlin.math.roundToInt

object Converter {

    private val formatter = DecimalFormat("#.00")

    fun getFormattedHeight(height: Int): String {
        val SIHeight = if (height >= 10) "${formatter.format(height / 10F)} m" else "${(height * 10)} cm"
        var inches = height * 3.937F
        val feet = inches.roundToInt() / 12
        inches -= 12 * feet
        val USCHeight = "$feet ft ${inches.roundToInt()} in"
        return ("$SIHeight ($USCHeight)")
    }

    fun getFormattedWeight(weight: Int): String {
        val kilograms = weight / 10F
        val pounds = kilograms * 2.2046
        return "$kilograms kg (${formatter.format(pounds)} lbs)"
    }

}