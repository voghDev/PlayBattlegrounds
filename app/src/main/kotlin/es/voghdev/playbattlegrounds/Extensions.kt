package es.voghdev.playbattlegrounds

import android.app.Activity
import android.support.v4.app.Fragment

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