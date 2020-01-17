package com.vrgsoft.redditstop.data;

import com.vrgsoft.redditstop.data.model.Post;

import java.util.List;

public interface OnDataUpdateCallback {

    void onDataUpdate(List<Post> posts);

    void onConnectionFailed();
}
