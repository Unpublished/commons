package com.unpublished.commons

import android.content.Context
import android.util.Log
import java.util.zip.ZipFile

fun detectBloat(ctx: Context) {
    val sourceDir = ctx.applicationInfo.sourceDir
    ZipFile(sourceDir).use {
        val bloatFiles = listOf(
                "DebugProbesKt.bin", // https://github.com/Kotlin/kotlinx.coroutines/issues/2274
                "kotlin-tooling-metadata.json", // https://github.com/Kotlin/kotlinx.coroutines/issues/3158
        )
        for (file in bloatFiles) {
            val entry = it.getEntry(file)
            if (entry != null) {
                Log.i("BloatDetector", "detected $file")
            }
        }
    }
}
