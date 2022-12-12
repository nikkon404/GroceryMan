package com.nikkon.groceryman.Utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class AppSnackBar {
    public static void showSnack(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
