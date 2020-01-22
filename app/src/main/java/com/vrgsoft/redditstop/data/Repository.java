package com.vrgsoft.redditstop.data;

import android.graphics.Bitmap;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.vrgsoft.redditstop.data.cache.ImageCache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

public class Repository {

    private static Repository sRepository;

    private ThumbnailDownloader<View> mThumbnailDownloader;
    private ImageCache mImageCache;

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

    public Repository() {
        final int cacheSize = (int)Runtime.getRuntime().maxMemory()/1024/8;
        mImageCache = new ImageCache(cacheSize);
    }

    public ThumbnailDownloader<View> getThumbnailDownloader() {
        return mThumbnailDownloader;
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
        mThumbnailDownloader = new ThumbnailDownloader<>(handler, mImageCache);
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

    public void getJSONData(OnDataUpdateCallback callback, String[]... queryParams) {
        DataDownloader dataDownloader = new DataDownloader(callback);
        dataDownloader.getPosts(queryParams);
    }
}
