package com.vrgsoft.redditstop.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vrgsoft.redditstop.R;
import com.vrgsoft.redditstop.Utils;
import com.vrgsoft.redditstop.data.ThumbnailDownloader;
import com.vrgsoft.redditstop.data.model.Post;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TopPostsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int LOADING_TYPE = 1;
    private final int ITEM_TYPE = 0;

    private boolean isWaitForData = false;
    private List<Post> mPostList;
    private PostImageClickCallback mPostImageClickCallback;
    private ThumbnailDownloader<View> mThumbnailDownloader;

    public TopPostsListAdapter(PostImageClickCallback themeClickCallback) {
        mPostImageClickCallback = themeClickCallback;
    }

    public void setThumbnailDownloader(ThumbnailDownloader<View> thumbnailDownloader) {
        mThumbnailDownloader = thumbnailDownloader;
    }

    public void setUpAdaptersData(List<Post> posts){
        if (isWaitForData){
            removeLoadingItem();
        }
        if (mPostList == null) {
            mPostList = new ArrayList<>();
            mPostList.addAll(posts);
        }else {
            mPostList.addAll(posts);
        }
        notifyDataSetChanged();
    }
    private void addLoadingItem(){
        mPostList.add(null);
        notifyItemInserted(mPostList.size()-1);
    }

    public void removeLoadingItem(){
        mPostList.remove(mPostList.size()-1);
        notifyItemRemoved(mPostList.size());
    }

    public boolean isWaitForData() {
        return isWaitForData;
    }

    public void setWaitForData(boolean waitForData) {
        if (waitForData){
            addLoadingItem();
        }
        isWaitForData = waitForData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post, parent, false);
            return new PostHolder(view, mPostImageClickCallback, mThumbnailDownloader);
        }else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_loading, parent, false);
            return new LoadingItemHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PostHolder) {
            Post post = mPostList.get(position);
            PostHolder h = (PostHolder)holder;
            h.bind(post);
            if (post.hasImage()){
                h.setImage(post);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mPostList == null ? 0 : mPostList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mPostList.get(position) == null ? LOADING_TYPE : ITEM_TYPE;
    }

    static class PostHolder extends RecyclerView.ViewHolder{

        private TextView mAuthor;
        private TextView mTime;
        private TextView mTitle;
        private ImageView mImage;
        private TextView mComments;
        private PostImageClickCallback mPostImageClickCallback;
        private ThumbnailDownloader<View> mThumbnailDownloader;

        public PostHolder(@NonNull View itemView,
                          final PostImageClickCallback postImageClickCallback,
                          ThumbnailDownloader<View> thumbnailDownloader) {
            super(itemView);
            this.mPostImageClickCallback = postImageClickCallback;
            mAuthor = itemView.findViewById(R.id.author);
            mTime = itemView.findViewById(R.id.time);
            mTitle = itemView.findViewById(R.id.title);
            mImage = itemView.findViewById(R.id.image);
            mComments = itemView.findViewById(R.id.comments);
            mThumbnailDownloader = thumbnailDownloader;
        }

        public void bind(final Post post) {
            mAuthor.setText(post.getAuthor());
            String time = Utils.getXTimeAgo(post.getPostTime());
            mTime.setText(time);
            mTitle.setText(post.getTitle());
            mComments.setText(String.valueOf(post.getCommentsCount()));
        }

        public void setImage(final Post post){
            if (mThumbnailDownloader != null) {
                this.mThumbnailDownloader.loadThumbnail(mImage, post.getThumbnailUrl());
                if (post.hasImage()) {
                    mImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPostImageClickCallback.onClick(post);
                        }
                    });
                }
            }
        }
    }

    static class LoadingItemHolder extends RecyclerView.ViewHolder{

        private ProgressBar mProgressBar;

        public LoadingItemHolder(@NonNull View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.post_loading_progress_bar);
        }
    }
}
