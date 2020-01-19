package com.vrgsoft.redditstop.ui.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.view.View;

import com.vrgsoft.redditstop.data.OnDataUpdateCallback;
import com.vrgsoft.redditstop.data.OnImageLoadCallback;
import com.vrgsoft.redditstop.data.Repository;
import com.vrgsoft.redditstop.data.ThumbnailDownloader;
import com.vrgsoft.redditstop.data.model.Post;
import com.vrgsoft.redditstop.ui.ImageFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class TopPostsListViewModel extends AndroidViewModel {

    public static final int LOADER_ID = 1;
    private Repository mRepository;

    private List<Post> mPosts;
    private String mUrl;

    public TopPostsListViewModel(@NonNull Application application) {
        super(application);
        mRepository = Repository.getInstance();
    }

    public void selectPostsImageUrl(String url){
        mUrl = url;
    }
    public String getSelectedImageUrl(){
        return mUrl;
    }

    public void getPosts(OnDataUpdateCallback callback){
        mRepository.getJSONData(callback);
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
}
