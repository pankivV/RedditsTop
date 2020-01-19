package com.vrgsoft.redditstop.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.vrgsoft.redditstop.ImageLoader;
import com.vrgsoft.redditstop.data.model.Post;

import java.util.List;
import java.util.logging.LogRecord;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import static com.vrgsoft.redditstop.ui.ImageFragment.LOADER_ID;
import static com.vrgsoft.redditstop.ui.ImageFragment.URL_KEY;

public class Repository {

    private static Repository sRepository;

    private ThumbnailDownloader<View> mThumbnailDownloader;

    private List<Post> mPosts;

    public static Repository getInstance() {
        if (sRepository == null) {
            synchronized (Repository.class) {
                if (sRepository == null) {
                    sRepository = new Repository();
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
        DataDownloader dataDownloader = new DataDownloader(onDataUpdateCallback);
        dataDownloader.getPosts();
    }

    public void initImageLoaderTask(final Fragment fragment, int id, String url, final OnImageLoadCallback callbacks){
        Bundle args = new Bundle();
        args.putString(String.valueOf(id), url);
        final Loader<Bitmap> loader = LoaderManager.getInstance(fragment).initLoader(id, args, new LoaderManager.LoaderCallbacks<Bitmap>() {
            @NonNull
            @Override
            public Loader<Bitmap> onCreateLoader(int id, @Nullable Bundle args) {
                return new ImageLoader(fragment.getContext(), args.getString(String.valueOf(id)));
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Bitmap> loader, Bitmap data) {
                callbacks.onImageLoaded(data);
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Bitmap> loader) {

            }
        });
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

    public void uiViewDestroyed() {
        mThumbnailDownloader.clearQueue();
    }
}
