package com.unpublished.commons

import android.os.Looper
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers

fun replaceMainThreadScheduler() {
    RxAndroidPlugins.setInitMainThreadSchedulerHandler {
        AndroidSchedulers.from(
            Looper.getMainLooper(),
            true
        )
    }
}