package com.chat.joycom

import android.app.Activity
import android.app.Application
import android.os.Bundle
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class JoyComApplication : Application() {

    private val TAG = this::class.java.simpleName

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        monitorActivityLifeCycle()
    }

    private fun monitorActivityLifeCycle() {
        registerActivityLifecycleCallbacks(
            object : ActivityLifecycleCallbacks {
                override fun onActivityCreated(
                    activity: Activity,
                    savedInstanceState: Bundle?,
                ) {
                    Timber.tag(TAG).i("$activity create")
                }

                override fun onActivityStarted(activity: Activity) {
                    Timber.tag(TAG).i("$activity start")
                }

                override fun onActivityResumed(activity: Activity) {
                    Timber.tag(TAG).i("$activity resume")
                }

                override fun onActivityPaused(activity: Activity) {
                    Timber.tag(TAG).i("$activity pause")
                }

                override fun onActivityStopped(activity: Activity) {
                    Timber.tag(TAG).i("$activity stop")
                }

                override fun onActivityDestroyed(activity: Activity) {
                    Timber.tag(TAG).i("$activity destroy")
                }

                override fun onActivitySaveInstanceState(
                    activity: Activity,
                    outState: Bundle,
                ) {
                    Timber.tag(TAG).i("$activity save")
                }
            },
        )
    }
}