package es.voghdev.playbattlegrounds

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.EXTRA_STREAM
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.net.Uri
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.io.FileOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat

fun Activity.takeAScreenshot(path: String) {
    try {
        val imageFile = File(path)
        imageFile.createNewFile()
        with(window.decorView.rootView) {
            isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(drawingCache)
            isDrawingCacheEnabled = false

            val outputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

            outputStream.close()
        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}

fun Activity.shareFileNougat(message: String, path: String) {
    val uri: Uri = FileProvider.getUriForFile(
        this,
        "${BuildConfig.APPLICATION_ID}.provider",
        File(path))
    val intent = Intent(ACTION_SEND).apply {
        setDataAndType(uri, "image/*")
        putExtra(EXTRA_STREAM, uri)
    }

    startActivity(Intent.createChooser(intent, message))
}

fun Activity.shareFilePreNougat(message: String, path: String) {
    val uri: Uri = Uri.fromFile(File(path))
    val intent = Intent(ACTION_SEND).apply {
        setDataAndType(uri, "image/*")
        putExtra(EXTRA_STREAM, uri)
    }

    startActivity(Intent.createChooser(intent, message))
}

fun Activity.ui(action: () -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        action()
    }
}

fun Fragment.ui(action: () -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        action()
    }
}

fun Float.format(digits: Int): String = java.lang.String.format("%.${digits}f", this)

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

fun Int.minutes(): Long = this * 60000L

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

fun Context.screenWidth() = resources.displayMetrics.widthPixels

fun Context.screenHeight() = resources.displayMetrics.heightPixels

fun Context.screenDensity() = resources.displayMetrics.density