package com.nikkon.groceryman.Utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Converter {

    //bitmap image into base64
    public static String encodeImage(Bitmap bm) {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bs);
        byte[] b = bs.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    //base64 into image
    public static Bitmap decodeImage(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    //date in MMMM dd, yyyy format from long
    public static String getReadableDate(Date date){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
        return sdf.format(date);

    }
}
