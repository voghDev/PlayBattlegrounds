package es.voghdev.playbattlegrounds

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.inputmethod.InputMethodManager
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
        val df = SimpleDateFormat(format)
        val d = df.parse(this)
        return d.time
    } catch (e: ParseException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return 0L
}

fun Long.toDate(format: String = "yyyy/MM/dd") = SimpleDateFormat(format).format(this)