package com.vrgsoft.redditstop.data.cache;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.collection.LruCache;

public class ImageCache extends LruCache<String, Bitmap> {
    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public ImageCache(int maxSize) {
        super(maxSize);
    }

    public Bitmap getBitmapFromCache(String key){
        return this.get(key);
    }

    public void putBitmapInCache(String key, Bitmap bitmap){
        if (this.get(key) == null){
            this.put(key, bitmap);
        }
    }

    @Override
    protected int sizeOf(@NonNull String key, @NonNull Bitmap value) {
        return value.getByteCount()/1024;
    }
}
