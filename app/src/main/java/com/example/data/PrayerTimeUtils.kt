package com.example.data

import java.util.*
import kotlin.math.*

enum class CalculationMethod(val displayNameAr: String, val displayNameEn: String, val fajrAngle: Double, val ishaAngle: Double) {
    EGYPT("الهيئة المصرية العامة للمساحة", "Egyptian General Authority", 19.5, 17.5),
    UMM_AL_QURA("جامعة أم القرى (مكة المكرمة)", "Umm Al-Qura University", 18.5, 0.0), // Isha is 90 mins after Maghrib
    MWL("رابطة العالم الإسلامي", "Muslim World League", 18.0, 17.0),
    ISNA("الجمعية الإسلامية لأمريكا الشمالية", "ISNA", 15.0, 15.0)
}

enum class JuristicMethod(val displayNameAr: String, val displayNameEn: String, val shadowFactor: Double) {
    STANDARD("جمهور العلماء (شافعي، مالكي، حنبلي)", "Standard (Shafi'i, Maliki, Hanbali)", 1.0),
    HANAFI("المذهب الحنفي", "Hanafi", 2.0)
}

data class PrayerTimes(
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String
)

data class City(
    val nameAr: String,
    val nameEn: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: Double
) {
    companion object {
        val PREDEFINED_CITIES = listOf(
            City("مكة المكرمة", "Mecca", 21.4225, 39.8262, 3.0),
            City("المدينة المنورة", "Medina", 24.4672, 39.6111, 3.0),
            City("القاهرة", "Cairo", 30.0444, 31.2357, 3.0),
            City("الرياض", "Riyadh", 24.7136, 46.6753, 3.0),
            City("القدس الشريف", "Jerusalem", 31.7683, 35.2137, 3.0),
            City("دبي", "Dubai", 25.2048, 55.2708, 4.0),
            City("الدار البيضاء", "Casablanca", 33.5731, -7.5898, 1.0),
            City("إسطنبول", "Istanbul", 41.0082, 28.9784, 3.0),
            City("لندن", "London", 51.5074, -0.1278, 1.0),
            City("نيويورك", "New York", 40.7128, -74.0060, -4.0)
        )
    }
}

object PrayerTimeUtils {

    fun calculatePrayerTimes(
        lat: Double,
        lng: Double,
        timezone: Double,
        date: Calendar,
        calcMethod: CalculationMethod = CalculationMethod.EGYPT,
        juristicMethod: JuristicMethod = JuristicMethod.STANDARD
    ): PrayerTimes {
        val dayOfYear = date.get(Calendar.DAY_OF_YEAR).toDouble()

        // Astronomical calculations
        // 1. Mean anomaly of the sun (g)
        val g = Math.toRadians(357.529 + 0.98560028 * dayOfYear)
        // 2. Mean longitude of the sun (q)
        val q = 280.459 + 0.98564736 * dayOfYear
        // 3. Apparent longitude of the sun (L)
        val L = Math.toRadians(q + 1.915 * sin(g) + 0.02 * sin(2 * g))
        // 4. Obliquity of the ecliptic (e)
        val e = Math.toRadians(23.439 - 0.00000036 * dayOfYear)

        // Right ascension of the sun (double representation)
        val ra = atan2(cos(e) * sin(L), cos(L))
        var raHours = Math.toDegrees(ra) / 15.0
        if (raHours < 0) raHours += 24.0

        // Equation of Time (EqT)
        val eqT = (q / 15.0) - raHours

        // Sun declination (Dec)
        val dec = asin(sin(e) * sin(L))

        // Midday (Dhuhr)
        val midday = 12.0 + timezone - (lng / 15.0) - eqT
        val dhuhrHour = (midday + 24.0) % 24.0

        // Helper function for sunrise, sunset, Fajr, Isha
        fun hourAngle(altitude: Double): Double {
            val hRad = Math.toRadians(altitude)
            val lRad = Math.toRadians(lat)
            val numerator = sin(hRad) - sin(lRad) * sin(dec)
            val denominator = cos(lRad) * cos(dec)
            val cosH = numerator / denominator

            if (cosH > 1.0 || cosH < -1.0) return 0.0 // Sun never rises or sets here
            return Math.toDegrees(acos(cosH)) / 15.0
        }

        // Sunrise & Sunset (altitude -0.833)
        val hRiseSet = hourAngle(-0.833)
        val sunriseHour = (dhuhrHour - hRiseSet + 24.0) % 24.0
        val sunsetHour = (dhuhrHour + hRiseSet + 24.0) % 24.0

        // Fajr (altitude = -fajrAngle)
        val hFajr = hourAngle(-calcMethod.fajrAngle)
        val fajrHour = (dhuhrHour - hFajr + 24.0) % 24.0

        // Isha
        val ishaHour = if (calcMethod == CalculationMethod.UMM_AL_QURA) {
            // Umm Al-Qura: 90 minutes after Maghrib (Sunset)
            (sunsetHour + 1.5 + 24.0) % 24.0
        } else {
            val hIsha = hourAngle(-calcMethod.ishaAngle)
            (dhuhrHour + hIsha + 24.0) % 24.0
        }

        // Asr (Standard / Hanafi shadow equation)
        val lRad = Math.toRadians(lat)
        val diffDec = abs(lRad - dec)
        val cotDiff = 1.0 / tan(diffDec)
        val acRad = acos((sin(atan(1.0 / (juristicMethod.shadowFactor + tan(diffDec)))) - sin(lRad) * sin(dec)) / (cos(lRad) * cos(dec)))
        val hAsr = Math.toDegrees(acRad) / 15.0
        val asrHour = (dhuhrHour + hAsr + 24.0) % 24.0

        return PrayerTimes(
            fajr = formatTime(fajrHour),
            sunrise = formatTime(sunriseHour),
            dhuhr = formatTime(dhuhrHour),
            asr = formatTime(asrHour),
            maghrib = formatTime(sunsetHour),
            isha = formatTime(ishaHour)
        )
    }

    private fun formatTime(hourFraction: Double): String {
        var h = hourFraction.toInt()
        val minutesTotal = round((hourFraction - h) * 60).toInt()
        var m = minutesTotal
        if (m >= 60) {
            h += 1
            m -= 60
        }
        h = h % 24
        return String.format(Locale.US, "%02d:%02d", h, m)
    }

    // Calculate Qibla bearing from current coordinates to Mecca (21.4225 N, 39.8262 E)
    fun calculateQiblaDirection(userLat: Double, userLng: Double): Double {
        val meccaLat = Math.toRadians(21.4225)
        val meccaLng = Math.toRadians(39.8262)
        val uLat = Math.toRadians(userLat)
        val uLng = Math.toRadians(userLng)

        val deltaLng = meccaLng - uLng

        val y = sin(deltaLng) * cos(meccaLat)
        val x = cos(uLat) * sin(meccaLat) - sin(uLat) * cos(meccaLat) * cos(deltaLng)

        var qiblaAngle = Math.toDegrees(atan2(y, x))
        qiblaAngle = (qiblaAngle + 360.0) % 360.0
        return qiblaAngle
    }
}
