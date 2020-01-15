package com.vrgsoft.redditstop.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.vrgsoft.redditstop.R;
import com.vrgsoft.redditstop.data.model.Post;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TopPostsListFragment extends Fragment implements PostImageClickCallback {

    private ProgressBar mProgressBar;
    private TopPostsListAdapter mListAdapter;
    private RecyclerView mListContainer;

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
        mListAdapter = new TopPostsListAdapter(this);
        mListContainer.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mListContainer.setAdapter(mListAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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
