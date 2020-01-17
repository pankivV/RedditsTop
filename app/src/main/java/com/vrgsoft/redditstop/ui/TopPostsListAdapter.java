package com.vrgsoft.redditstop.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vrgsoft.redditstop.R;
import com.vrgsoft.redditstop.data.ThumbnailDownloader;
import com.vrgsoft.redditstop.data.model.Post;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.vrgsoft.redditstop.data.RedditJSONKeyNames.SELF;

public class TopPostsListAdapter extends RecyclerView.Adapter<TopPostsListAdapter.PostHolder> {

    private List<Post> mPostList = new ArrayList<>();
    private PostImageClickCallback mPostImageClickCallback;
    private ThumbnailDownloader<View> mThumbnailDownloader;
    public TopPostsListAdapter(PostImageClickCallback themeClickCallback) {
        mPostImageClickCallback = themeClickCallback;
    }

    public void setThumbnailDownloader(ThumbnailDownloader<View> thumbnailDownloader) {
        mThumbnailDownloader = thumbnailDownloader;
    }

    public void setUpAdaptersData(List<Post> posts){
        mPostList = posts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post, parent, false);
        return new PostHolder(view, mPostImageClickCallback, mThumbnailDownloader);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        Post post = mPostList.get(position);
        holder.bind(post);
        if (post.hasImage()){
            holder.setImage(post);
        }
    }

    @Override
    public int getItemCount() {
        return mPostList == null ? 0 : mPostList.size();
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
            mTime.setText(String.valueOf(post.getPostTime()));
            mTitle.setText(post.getTitle());
            /*mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPostImageClickCallback.onClick(post);
                }
            });*/
            mComments.setText(String.valueOf(post.getCommentsCount()));
        }

        public void setImage(Post post){
            if (mThumbnailDownloader != null) {
                this.mThumbnailDownloader.loadThumbnail(mImage, post.getThumbnailUrl());
            }
        }
    }
}
