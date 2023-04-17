package com.unpublished.commons

import android.content.Context
import android.content.pm.ApplicationInfo
import android.util.Log
import java.util.zip.ZipFile

fun detectBloat(ctx: Context) {
    val applicationInfo = ctx.applicationInfo
    val sourceDir = applicationInfo.sourceDir
    ZipFile(sourceDir).use {
        val bloatFiles = buildBloatList(applicationInfo)
        for (file in bloatFiles) {
            val entry = it.getEntry(file)
            if (entry != null) {
                Log.i("BloatDetector", "detected $file")
            }
        }
    }
}

private fun buildBloatList(applicationInfo: ApplicationInfo): List<String> {
    val alwaysBloat = listOf(
        "DebugProbesKt.bin", // https://github.com/Kotlin/kotlinx.coroutines/issues/2274
        "kotlin-tooling-metadata.json", // https://github.com/Kotlin/kotlinx.coroutines/issues/3158
    )
    val isNotDebuggable = applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE == 0
    return if (!isNotDebuggable) {
        val releaseBloat = listOf(
            "META-INF/androidx.core_core.version", // https://github.com/android/sunflower/pull/804
        )
        val bloatFiles = ArrayList<String>(alwaysBloat.size + releaseBloat.size)
        bloatFiles.addAll(alwaysBloat)
        bloatFiles.addAll(releaseBloat)
        bloatFiles
    } else {
        alwaysBloat
    }
}
