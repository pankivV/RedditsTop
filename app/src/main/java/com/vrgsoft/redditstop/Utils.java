package com.vrgsoft.redditstop;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;

public class Utils {

    public static String getHMSTime(long time){

        long timeInSec = (System.currentTimeMillis()/1000) - time;
        long hour = (timeInSec - (timeInSec % 3600)) / 3600;
        long min = ((timeInSec % 3600) - ((timeInSec % 3600) % 60)) / 60;
        long sec = timeInSec % 60;
        StringBuilder builder = new StringBuilder();
        builder.append(hour).append('h')
                .append(min).append('m')
                .append(sec).append('s');
        return builder.toString();
    }

    public static void saveImageToGallery(Context context, Bitmap source, String title, String description){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);

        Uri url = null;
        ContentResolver contentResolver = context.getContentResolver();
        try{
            url = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            OutputStream outputStream = contentResolver.openOutputStream(url);
            try {
                source.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            }finally {
                outputStream.close();
            }
            Toast.makeText(
                    context,
                    context.getResources().getText(R.string.image_saved_message), Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            contentResolver.delete(url, null, null);
            Toast.makeText(
                    context,
                    context.getResources().getText(R.string.image_saving_error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public static int getXHour(long postTime) {
        long timeInSec = (System.currentTimeMillis()/1000) - postTime;
        long hour = (timeInSec - (timeInSec % 3600)) / 3600;
        return (int)hour;
    }
}
