package es.voghdev.playbattlegrounds.common.ui

import com.google.android.material.snackbar.Snackbar
import android.view.View

object ColoredSnackbar {

    private const val red = 0xff_f4_43_36.toInt()
    private const val green = 0xff_4c_af_50.toInt()
    private const val blue = 0xff_21_95_f3.toInt()
    private const val orange = 0xff_ff_c1_07.toInt()

    private fun getSnackBarLayout(snackbar: Snackbar?): View? = snackbar?.view

    private fun colorSnackBar(snackbar: Snackbar, colorId: Int, bold: Boolean = false): Snackbar {
        val snackBarView = getSnackBarLayout(snackbar)
        if (snackBarView != null) {
            snackBarView.setBackgroundColor(colorId)
        }

        return snackbar
    }

    fun info(snackbar: Snackbar): Snackbar = colorSnackBar(snackbar, blue)

    fun warning(snackbar: Snackbar): Snackbar = colorSnackBar(snackbar, orange)

    fun warningBold(snackbar: Snackbar): Snackbar = colorSnackBar(snackbar, orange, true)

    fun alert(snackbar: Snackbar): Snackbar = colorSnackBar(snackbar, red)

    fun alertBold(snackbar: Snackbar): Snackbar = colorSnackBar(snackbar, red, true)

    fun confirm(snackbar: Snackbar): Snackbar = colorSnackBar(snackbar, green)
}
