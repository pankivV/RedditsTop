package com.vrgsoft.redditstop.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.vrgsoft.redditstop.data.cache.ImageCache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import androidx.annotation.NonNull;

public class ThumbnailDownloader<T> extends HandlerThread {

    private static final String TAG = "ThumbnailDownloader";
    private static final int DOWNLOAD_MESSAGE = 0;

    private boolean mHasQuit = false;
    private RequestHandler<T> mRequestHandler;
    private ConcurrentMap<T,String> mRequestMap = new ConcurrentHashMap<>();
    private ThumbnailDownloadListener mThumbnailDownloadListener;
    private Handler mUIHandler;
    private ImageCache mImageCache;

    public ThumbnailDownloader(Handler handler, ImageCache lruCache) {
        super(TAG);
        mUIHandler = handler;
        mImageCache = lruCache;
        //Log.i(TAG, "ThumbnailDownloader: new ThreadHandler created");
    }

    public void loadThumbnail(T t, String url){
        if (url == null) {
            mRequestMap.remove(t);
        }else {
            mRequestMap.put(t, url);
            mRequestHandler.obtainMessage(DOWNLOAD_MESSAGE, t).sendToTarget();
        }
    }

    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new RequestHandler<T>(new RequestHandler.RequestHandlerListener<T>() {
            @Override
            public void onHandleRequest(T t) {
                handleRequest(t);
            }
        });
    }

    @Override
    public boolean quit() {
        mHasQuit = true;
        return super.quit();
    }

    public interface ThumbnailDownloadListener<T>{
        void onThumbnailDownloaded(T t, Bitmap bitmap);
    }

    public void setThumbnailDownloadListener(ThumbnailDownloadListener thumbnailDownloadListener) {
        mThumbnailDownloadListener = thumbnailDownloadListener;
    }

    static class RequestHandler<T> extends Handler{

        private RequestHandlerListener<T> mListener;

        public RequestHandler(RequestHandlerListener<T> listener) {
            mListener = listener;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == DOWNLOAD_MESSAGE){
                T t = (T) msg.obj;
                mListener.onHandleRequest(t);
            }
        }
        public interface RequestHandlerListener<T>{
            void onHandleRequest(T t);
        }
    }

    public void clearQueue(){
        mRequestHandler.removeMessages(DOWNLOAD_MESSAGE);
        mRequestMap.clear();
    }
    private void handleRequest(final T t){
        final String url = mRequestMap.get(t);
        if (url == null) {
            return;
        }
        Bitmap bitmap = mImageCache.getBitmapFromCache(url);
        if (bitmap == null) {
            byte[] bitmapBytes = new DataDownloader(null).getUrlBytes(url);
            bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            mImageCache.putBitmapInCache(url, bitmap);
            //Log.i(TAG, "handleRequest: pull image from network");
        }else {
            //Log.i(TAG, "handleRequest: pull image from cache");
        }
        final Bitmap finalBitmap = bitmap;
        mUIHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mRequestMap.get(t) != url || mHasQuit){
                    return;
                }
                mRequestMap.remove(t);
                mThumbnailDownloadListener.onThumbnailDownloaded(t, finalBitmap);
            }
        });
    }
}
