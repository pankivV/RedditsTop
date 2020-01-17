package com.vrgsoft.redditstop.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vrgsoft.redditstop.R;
import com.vrgsoft.redditstop.data.DataDownloader;
import com.vrgsoft.redditstop.data.OnDataUpdateCallback;
import com.vrgsoft.redditstop.data.ThumbnailDownloader;
import com.vrgsoft.redditstop.data.model.Post;
import com.vrgsoft.redditstop.ui.viewmodel.TopPostsListViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TopPostsListFragment extends Fragment implements PostImageClickCallback{

    private ProgressBar mProgressBar;
    private TopPostsListAdapter mListAdapter;
    private RecyclerView mListContainer;
    private TopPostsListViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_posts_list_fragment,
                container, false);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mListContainer = view.findViewById(R.id.top_posts_list);
        setProgressView(true);
        mListAdapter = new TopPostsListAdapter(this);
        mListContainer.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mListContainer.setAdapter(mListAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TopPostsListViewModel.class);
        Handler handler = new Handler();
        mViewModel.initDownloadTask(handler, new ThumbnailDownloader.ThumbnailDownloadListener<View>() {
            @Override
            public void onThumbnailDownloaded(View view, Bitmap bitmap) {
                ((ImageView)view).setImageBitmap(bitmap);
            }
        });
        mListAdapter.setThumbnailDownloader(mViewModel.getThumbnailDownloader());
        mViewModel.getPosts(new OnDataUpdateCallback() {
            @Override
            public void onDataUpdate(List<Post> posts) {
                mListAdapter.setUpAdaptersData(posts);
                setProgressView(false);
            }

            @Override
            public void onConnectionFailed() {
                Toast.makeText(getActivity(), "Error on Reddit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.onConfigurationChanged();
    }

    public void setProgressView(boolean show){
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mListContainer.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    //callback for image click
    @Override
    public void onClick(Post post) {

    }
}
