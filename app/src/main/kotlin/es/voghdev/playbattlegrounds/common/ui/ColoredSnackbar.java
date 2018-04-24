package es.voghdev.playbattlegrounds.common.ui;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

public class ColoredSnackbar {

    private static final int red = 0xfff44336;
    private static final int green = 0xff4caf50;
    private static final int blue = 0xff2195f3;
    private static final int orange = 0xffffc107;

    private static View getSnackBarLayout(Snackbar snackbar) {
        if (snackbar != null) {
            return snackbar.getView();
        }
        return null;
    }

    private static Snackbar colorSnackBar(Snackbar snackbar, int colorId) {
        return colorSnackBar(snackbar, colorId, false);
    }

    private static Snackbar colorSnackBar(Snackbar snackbar, int colorId, boolean bold) {
        View snackBarView = getSnackBarLayout(snackbar);
        if (snackBarView != null) {
            TextView snackBarTextView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            snackBarView.setBackgroundColor(colorId);
            snackBarTextView.setTypeface(null, bold ? Typeface.BOLD : Typeface.NORMAL);
            snackBarTextView.setTextColor(Color.WHITE);
        }

        return snackbar;
    }

    public static Snackbar info(Snackbar snackbar) {
        return colorSnackBar(snackbar, blue);
    }

    public static Snackbar warning(Snackbar snackbar) {
        return colorSnackBar(snackbar, orange);
    }

    public static Snackbar warningBold(Snackbar snackbar) {
        return colorSnackBar(snackbar, orange, true);
    }

    public static Snackbar alert(Snackbar snackbar) {
        return colorSnackBar(snackbar, red);
    }

    public static Snackbar alertBold(Snackbar snackbar) {
        return colorSnackBar(snackbar, red, true);
    }

    public static Snackbar confirm(Snackbar snackbar) {
        return colorSnackBar(snackbar, green);
    }
}