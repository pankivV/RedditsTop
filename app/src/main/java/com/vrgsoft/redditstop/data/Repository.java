package com.vrgsoft.redditstop.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.vrgsoft.redditstop.data.model.Post;

import java.util.List;
import java.util.logging.LogRecord;

import androidx.lifecycle.LiveData;

public class Repository {

    private static Repository sRepository;

    private ThumbnailDownloader<View> mThumbnailDownloader;
    private DataDownloader mDataDownloader;

    private List<Post> mPosts;
    private Context mContext;

    public Repository(Context context) {
        mContext = context;
    }

    public static Repository getInstance(final Context context) {
        if (sRepository == null) {
            synchronized (Repository.class) {
                if (sRepository == null) {
                    sRepository = new Repository(context);
                }
            }
        }
        return sRepository;
    }

    public List<Post> getPosts() {
        return mPosts;
    }

    public ThumbnailDownloader<View> getThumbnailDownloader() {
        return mThumbnailDownloader;
    }

    public void getJSONData(OnDataUpdateCallback onDataUpdateCallback){
        mDataDownloader = new DataDownloader(onDataUpdateCallback);
        mDataDownloader.getPosts();
    }

    public void initThumbnailDownloaderTask(Handler handler,ThumbnailDownloader.ThumbnailDownloadListener<View> listener){
        mThumbnailDownloader = new ThumbnailDownloader<>(handler);
        mThumbnailDownloader.setThumbnailDownloadListener(listener);
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
    }

    public void quitHandlerThread(){
        mThumbnailDownloader.quit();
    }
}
