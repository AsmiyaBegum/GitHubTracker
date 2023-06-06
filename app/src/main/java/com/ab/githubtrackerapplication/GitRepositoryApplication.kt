package com.ab.githubtrackerapplication

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.ab.githubtrackerapplication.util.Utils
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlin.system.exitProcess

class GitRepositoryApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .allowWritesOnUiThread(true)
            .build()

        Realm.setDefaultConfiguration(config)

        if (!Utils.isBuildVariantDebug()) {
            Thread.setDefaultUncaughtExceptionHandler { _, e ->
                handleUncaughtException(e)
                restartApp()
            }
        }
    }


    private fun handleUncaughtException(e: Throwable) {
        Log.e("app_crash", e.message.toString())
    }

    private fun restartApp() {
        val intent = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        exitProcess(1)
    }


    companion object {
        private var appContext: Context? = null

        fun appContext(): Context? {
            return appContext
        }

    }

}