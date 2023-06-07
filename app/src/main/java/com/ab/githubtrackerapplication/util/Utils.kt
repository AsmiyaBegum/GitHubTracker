package com.ab.githubtrackerapplication.util

import android.content.Context
import android.net.ConnectivityManager
import android.provider.SyncStateContract
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.ab.githubtrackerapplication.GitRepositoryApplication
import com.ab.githubtrackerapplication.R
import com.google.android.material.snackbar.Snackbar
import io.realm.BuildConfig
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun <T> realmDefaultInstance(block: (Realm) -> (T)): T {
        val realm = Realm.getDefaultInstance()
        if (realm.numberOfActiveVersions > 1) {
            logMessage(
                "more version",
                realm.numberOfActiveVersions.toString(),
                Constants.DEBUG_MODE
            )
        }
        try {
            return block(realm)
        } finally {
            realm.close()
        }
    }

    private fun allowLogging(): Boolean {
        return BuildConfig.DEBUG
    }

    fun isBuildVariantDebug(): Boolean = BuildConfig.BUILD_TYPE == Constants.BUILD_TYPE_DEBUG


    fun logMessage(tag: String, msg: String, mode: String) {
        if (allowLogging()) {
            when (mode) {
                Constants.DEBUG_MODE -> {
                    Log.d(tag, msg)
                }
                Constants.ERROR_MODE -> {
                    Log.e(tag, msg)
                }
                Constants.INFO_MODE -> {
                    Log.i(tag, msg)
                }
            }
        }
    }

    internal fun checkInternetConnection(): Boolean {
        val connectivityManager = GitRepositoryApplication.appContext()
            ?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isConnected = connectivityManager.activeNetworkInfo?.isConnected
        return isConnected ?: false
    }

    fun snackBarListener(
        view: View,
        text: String,
        snack: (Snackbar) -> Unit,
        okayButtonEvent: (Unit) -> Unit = { }
    ) {
        val snackBar = Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.key_okay) { action -> okayButtonEvent(Unit) }
        val textView = snackBar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setAllCaps(true)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        textView.maxLines = 10
        snackBar.show()
        snack(snackBar)
    }

    fun View.showVisibility(condition: Boolean) {
        this.visibility = if (condition) View.VISIBLE else View.GONE
    }

    fun List<View>.showView(condition: Boolean){
        this.forEach {
            it.showVisibility(condition)
        }
    }

    fun <T> Realm.evictResult(obj: RealmResults<T>): List<T> where T : RealmObject {
        return if (obj.isManaged) this.copyFromRealm(obj) else obj
    }

    fun TextView.formatDate(date : Date, dateFormat : String){
        val dateFormatter = SimpleDateFormat(dateFormat, Locale.US)
        val formattedDate: String = dateFormatter.format(date)
        this.text = formattedDate
    }
}