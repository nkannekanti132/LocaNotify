package com.example.locanotify.utils
import kotlin.math.*

object LocationUtils {
    private const val EARTH_RADIUS = 6371 // Earth radius in kilometers

    fun distanceBetweenPoints(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2) + sin(dLon / 2).pow(2) * cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return EARTH_RADIUS * c
    }

    fun isWithinRange(lat1: Double, lon1: Double, lat2: Double, lon2: Double, range: Double): Boolean {
        val distance = distanceBetweenPoints(lat1, lon1, lat2, lon2)
        return distance <= range
    }
}