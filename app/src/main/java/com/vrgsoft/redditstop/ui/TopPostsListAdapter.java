package com.vrgsoft.redditstop.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vrgsoft.redditstop.R;
import com.vrgsoft.redditstop.data.model.Post;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TopPostsListAdapter extends RecyclerView.Adapter<TopPostsListAdapter.PostHolder> {

    private List<Post> mPostList = new ArrayList<>();
    private PostImageClickCallback mPostImageClickCallback;

    public TopPostsListAdapter(PostImageClickCallback themeClickCallback) {
        mPostImageClickCallback = themeClickCallback;
    }


    public void setUpAdaptersData(List<Post> themeList){
        mPostList = themeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post, parent, false);
        return new PostHolder(view, mPostImageClickCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.bind(mPostList.get(position));
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

        public PostHolder(@NonNull View itemView, final PostImageClickCallback postImageClickCallback) {
            super(itemView);
            this.mPostImageClickCallback = postImageClickCallback;
            mAuthor = itemView.findViewById(R.id.author);
            mTime = itemView.findViewById(R.id.time);
            mTitle = itemView.findViewById(R.id.title);
            mImage = itemView.findViewById(R.id.image);
            mComments = itemView.findViewById(R.id.comments);
        }

        public void bind(final Post post) {
            mAuthor.setText(post.getAuthor());
            mTime.setText(post.getPostTime());
            mTitle.setText(post.getTitle());
            mImage.setImageBitmap(null);
            mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPostImageClickCallback.onClick(post);
                }
            });
            mComments.setText(String.valueOf(post.getCommentsCount()));
        }
    }
}
