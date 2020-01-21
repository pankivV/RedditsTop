package com.vrgsoft.redditstop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class ImageLoader extends AsyncTaskLoader<Bitmap> {

    private final String mUrl;
    private String TAG = getClass().getSimpleName();

    public ImageLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
        forceLoad();
    }

    @Nullable
    @Override
    public Bitmap loadInBackground() {
        byte[] bitmapBytes;
        try {
            bitmapBytes = getUrlBytes(mUrl);
        } catch (IOException e) {
            //Log.i(TAG, "loadInBackground: " + e.getMessage());
            return null;
        }
        return BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
    }

    public byte[] getUrlBytes(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //Log.i(TAG, "getUrlBytes: " + connection.getResponseCode());
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

}
