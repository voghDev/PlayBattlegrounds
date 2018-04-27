package es.voghdev.playbattlegrounds

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager

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

fun Context.hideSoftKeyboard(v: View) {
    val imm: InputMethodManager? = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm?.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
}
