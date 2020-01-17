package com.vrgsoft.redditstop.ui.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.view.View;

import com.vrgsoft.redditstop.data.OnDataUpdateCallback;
import com.vrgsoft.redditstop.data.Repository;
import com.vrgsoft.redditstop.data.ThumbnailDownloader;
import com.vrgsoft.redditstop.data.model.Post;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class TopPostsListViewModel extends AndroidViewModel {

    private Repository mRepository;

    private List<Post> mPosts;

    public TopPostsListViewModel(@NonNull Application application) {
        super(application);
        mRepository = Repository.getInstance(application.getApplicationContext());
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
}
