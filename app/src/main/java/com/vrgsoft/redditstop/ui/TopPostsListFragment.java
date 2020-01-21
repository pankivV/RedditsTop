package com.vrgsoft.redditstop.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vrgsoft.redditstop.MainActivity;
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

public class TopPostsListFragment extends Fragment implements PostImageClickCallback, OnDataUpdateCallback{
    private static final String TAG = "TopPostsListFragment";
    private ProgressBar mProgressBar;
    private TopPostsListAdapter mListAdapter;
    private RecyclerView mListContainer;
    private TopPostsListViewModel mViewModel;
    private LinearLayoutManager mLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_posts_list_fragment,
                container, false);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mListContainer = view.findViewById(R.id.top_posts_list);
        setProgressView(true);
        mListAdapter = new TopPostsListAdapter(getContext(),this);
        mLayoutManager = new LinearLayoutManager(getContext());
        mListContainer.setLayoutManager(mLayoutManager);
        mListContainer.setAdapter(mListAdapter);
        //listener for endless scroll/pagination
        mListContainer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mListAdapter.isWaitForData()) {
                    if (mLayoutManager.findLastVisibleItemPosition() == mLayoutManager.getItemCount() - 1) {
                        //Log.i(TAG, "onScrolled: last view");
                        mListAdapter.setWaitForData(true);
                        mViewModel.updatePosts(TopPostsListFragment.this);
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(TopPostsListViewModel.class);
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
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.onConfigurationChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.destroyView();
    }

    private void setProgressView(boolean show){
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mListContainer.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    //callback for image click
    @Override
    public void onClick(Post post) {
        mViewModel.selectPostsImageUrl(post.getHighResImageUrl());
        ((MainActivity)getActivity()).startImageFragment(post.getHighResImageUrl());
    }
    //callback for pagination
    @Override
    public void onDataUpdate(List<Post> posts) {
        mListAdapter.setUpAdaptersData(posts);
        mListAdapter.setWaitForData(false);
    }
}
