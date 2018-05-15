package es.voghdev.playbattlegrounds

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import java.text.ParseException
import java.text.SimpleDateFormat

fun Activity.ui(action: () -> Unit) {
    runOnUiThread {
        action()
    }
}

fun Fragment.ui(action: () -> Unit) {
    activity?.runOnUiThread {
        action()
    }
}

fun Fragment.colorStateList(colorResId: Int): ColorStateList {
    return ColorStateList.valueOf(ContextCompat.getColor(context!!, colorResId))
}

fun Context.colorStateList(colorResId: Int): ColorStateList {
    return ColorStateList.valueOf(ContextCompat.getColor(this, colorResId))
}

fun Context.hideSoftKeyboard(v: View) {
    val imm: InputMethodManager? = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm?.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
}

fun String.toDate(format: String): Long {
    try {
        val date = this.split("T").get(0)
        val time = this.split("T").get(1).removeSuffix("Z")
        val d = SimpleDateFormat(format).parse("$date $time")
        return d.time
    } catch (e: ParseException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return 0L
}

fun Long.toDate(format: String = "yyyy/MM/dd - HH:mm") = SimpleDateFormat(format).format(this)

fun Context.getPreferences(): SharedPreferences {
    return this.getSharedPreferences("PlayBattlegroundsPrefs", 0)
}

fun Context.putPreference(key: String, value: Boolean) {
    getPreferences().edit().putBoolean(key, value).apply()
}

fun Context.putPreference(key: String, value: String) {
    getPreferences().edit().putString(key, value).apply()
}

fun Context.putPreference(key: String, value: Int) {
    getPreferences().edit().putInt(key, value).apply()
}

fun Context.putPreference(key: String, value: Float) {
    getPreferences().edit().putFloat(key, value).apply()
}

fun Context.putPreference(key: String, value: Long) {
    getPreferences().edit().putLong(key, value).apply()
}

fun Context.getBooleanPreference(key: String): Boolean {
    return getPreferences().getBoolean(key, false)
}

fun Context.getStringPreference(key: String): String {
    return getPreferences().getString(key, "")
}

fun Context.getIntPreference(key: String, defaultValue: Int): Int {
    return getPreferences().getInt(key, defaultValue)
}

fun Context.getFloatPreference(key: String, defaultValue: Float): Float {
    return getPreferences().getFloat(key, defaultValue)
}

fun Context.getIntPreference(key: String): Int {
    return getPreferences().getInt(key, 0)
}

fun Context.getLongPreference(key: String): Long {
    return getPreferences().getLong(key, 0L)
}

fun Context.removePreference(key: String) {
    return getPreferences().edit().remove(key).apply()
}

fun Context.copyDatabaseToSDCard(dbName: String) {
    try {
        val sd = Environment.getExternalStorageDirectory()
        val data = Environment.getDataDirectory()

        if (sd.canWrite()) {
            val currentDBPath: String = "//data//" + BuildConfig.APPLICATION_ID + "//databases//" + dbName + ".db"
            val backupDBPath: String = dbName + "-backup.db"
            val currentDB: File = File(data, currentDBPath)
            val backupDB: File = File(sd, backupDBPath)

            if (currentDB.exists()) {
                val src: FileChannel = FileInputStream(currentDB).channel
                val dst: FileChannel = FileOutputStream(backupDB).channel
                dst.transferFrom(src, 0, src.size())
                src.close()
                dst.close()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}