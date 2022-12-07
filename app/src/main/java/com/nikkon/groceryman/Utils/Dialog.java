package com.nikkon.groceryman.Utils;


import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class Dialog {
    public static void show(Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}
