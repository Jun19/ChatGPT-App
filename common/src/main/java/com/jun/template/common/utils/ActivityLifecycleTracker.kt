package com.jun.template.common.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

object ActivityLifecycleTracker {
    private val tracking = AtomicBoolean(false)
    private var currActivity: WeakReference<Activity>? = null
    private var activityReferences = 0
    private val foregroundActivityCount = AtomicInteger(0)
    fun startTracking(application: Application) {
        if (!tracking.compareAndSet(false, true)) {
            return
        }
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                onActivityCreated(activity)
            }

            override fun onActivityStarted(activity: Activity) {
                activityReferences++
                ActivityLifecycleTracker.onActivityStarted(activity)
            }

            override fun onActivityResumed(activity: Activity) {
                ActivityLifecycleTracker.onActivityResumed(activity)
            }

            override fun onActivityPaused(activity: Activity) {
                ActivityLifecycleTracker.onActivityPaused(activity)
            }

            override fun onActivityStopped(activity: Activity) {
                ActivityLifecycleTracker.onActivityStopped(activity)
                activityReferences--
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                onActivitySaveInstanceState(activity)
            }

            override fun onActivityDestroyed(activity: Activity) {
                ActivityLifecycleTracker.onActivityDestroyed(activity)
            }
        })
    }

    val isInBackground: Boolean
        get() = 0 == activityReferences
    val currentActivity: Activity?
        get() = if (currActivity != null) currActivity!!.get() else null

    private fun onActivityCreated(activity: Activity) {}
    private fun onActivityStarted(activity: Activity) {}
    private fun onActivityResumed(activity: Activity) {
        currActivity = WeakReference(activity)
        foregroundActivityCount.incrementAndGet()
    }

    private fun onActivityPaused(activity: Activity) {
        val count = foregroundActivityCount.decrementAndGet()
        if (count < 0) {
            foregroundActivityCount.set(0)
        }
    }

    private fun onActivityStopped(activity: Activity) {}
    private fun onActivitySaveInstanceState(activity: Activity) {}
    private fun onActivityDestroyed(activity: Activity) {}
}