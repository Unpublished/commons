package com.unpublished.commons

import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy

@JvmOverloads
fun setupStrictMode(
    permitActivityLeaks: Boolean = false,
    permitNonSdkApiUsage: Boolean = false
) {
    StrictMode.enableDefaults()
    val threadPolicyBuilder = ThreadPolicy.Builder(StrictMode.getThreadPolicy())
    val badManufactures = hashSetOf("Google", "Nubia", "OnePlus", "Xiaomi").map { m -> m.lowercase() }
    val isBad = Build.MANUFACTURER.lowercase() in badManufactures
    if (!isBad) {
        threadPolicyBuilder.penaltyDeath()
    }
    StrictMode.setThreadPolicy(threadPolicyBuilder.build())
    val builder = VmPolicy.Builder().penaltyLog()
        .detectLeakedSqlLiteObjects()
        .detectLeakedClosableObjects()
        .detectLeakedRegistrationObjects()
        .detectFileUriExposure()

    if (!permitActivityLeaks) {
        builder.detectActivityLeaks()
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        builder.detectCleartextNetwork()
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        builder.detectContentUriWithoutPermission()
        builder.detectUntaggedSockets()
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !permitNonSdkApiUsage) {
        builder.detectNonSdkApiUsage()
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        builder.detectCredentialProtectedWhileLocked()
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        builder.detectIncorrectContextUse()
        builder.detectUnsafeIntentLaunch()
    }
    if (!isBad) {
        builder.penaltyDeath()
    }
    StrictMode.setVmPolicy(builder.build())
}
