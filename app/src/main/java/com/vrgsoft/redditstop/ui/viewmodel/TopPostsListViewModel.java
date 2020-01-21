package com.vrgsoft.redditstop.ui.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.vrgsoft.redditstop.data.OnDataUpdateCallback;
import com.vrgsoft.redditstop.data.OnImageLoadCallback;
import com.vrgsoft.redditstop.data.Repository;
import com.vrgsoft.redditstop.data.ThumbnailDownloader;
import com.vrgsoft.redditstop.data.model.Post;
import com.vrgsoft.redditstop.ui.ImageFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import static com.vrgsoft.redditstop.data.RedditJSONKeyNames.AFTER;

public class TopPostsListViewModel extends AndroidViewModel implements OnDataUpdateCallback {

    private static final String TAG = "TopPostsListViewModel";
    public static final int LOADER_ID = 1;
    private Repository mRepository;

    private List<Post> mPosts;
    private String mUrl;
    private OnDataUpdateCallback mUiCallback;

    public TopPostsListViewModel(@NonNull Application application) {
        super(application);
        mRepository = Repository.getInstance();
        mPosts = new ArrayList<>();
        //Log.i(TAG, "TopPostsListViewModel: new viewmodel created");
    }

    public void selectPostsImageUrl(String url){
        mUrl = url;
    }
    public String getSelectedImageUrl(){
        return mUrl;
    }

    public void getPosts(final OnDataUpdateCallback callback){
        mUiCallback = callback;
        if (mPosts.isEmpty()) {
            mRepository.getJSONData(this, null);
        }else {
            callback.onDataUpdate(mPosts);
        }
    }

    public void updatePosts(OnDataUpdateCallback callback){
        mUiCallback = callback;
        String paramValue = mPosts.get(mPosts.size()-1).getAfter();
        mRepository.getJSONData(this, new String[]{AFTER, paramValue});
    }


    public void onConfigurationChanged(){
        mRepository.quitHandlerThread();
    }

    public void initDownloadTask(Handler handler, ThumbnailDownloader.ThumbnailDownloadListener<View> listener) {
        mRepository.initThumbnailDownloaderTask(handler, listener);
    }

    public ThumbnailDownloader<View> getThumbnailDownloader() {
        return mRepository.getThumbnailDownloader();
    }

    public void destroyView() {
        mRepository.uiViewDestroyed();
    }

    public void initImageDownload(ImageFragment imageFragment, OnImageLoadCallback imageLoadCallback) {
        mRepository.initImageLoaderTask(imageFragment, LOADER_ID, getSelectedImageUrl(),imageLoadCallback);
    }

    @Override
    public void onDataUpdate(List<Post> posts) {
        mPosts.addAll(posts);
        mUiCallback.onDataUpdate(posts);
    }
}
