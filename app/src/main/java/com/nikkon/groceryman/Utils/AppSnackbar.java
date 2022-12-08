package com.nikkon.groceryman.Utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class AppSnackbar {
    public static void showSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
