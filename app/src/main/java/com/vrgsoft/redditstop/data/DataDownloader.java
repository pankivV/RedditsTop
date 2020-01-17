package com.vrgsoft.redditstop.data;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.vrgsoft.redditstop.data.model.Post;
import com.vrgsoft.redditstop.data.RedditJSONKeyNames;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.vrgsoft.redditstop.data.RedditJSONKeyNames.CHILDREN;
import static com.vrgsoft.redditstop.data.RedditJSONKeyNames.CREATED_UTC;
import static com.vrgsoft.redditstop.data.RedditJSONKeyNames.DATA;
import static com.vrgsoft.redditstop.data.RedditJSONKeyNames.NUM_COMMENTS;
import static com.vrgsoft.redditstop.data.RedditJSONKeyNames.SELF;
import static com.vrgsoft.redditstop.data.RedditJSONKeyNames.SUBREDDIT_NAME_PREFIXED;
import static com.vrgsoft.redditstop.data.RedditJSONKeyNames.THUMBNAIL;
import static com.vrgsoft.redditstop.data.RedditJSONKeyNames.THUMBNAIL_HEIGHT;
import static com.vrgsoft.redditstop.data.RedditJSONKeyNames.THUMBNAIL_WIDTH;
import static com.vrgsoft.redditstop.data.RedditJSONKeyNames.TITLE;

public class DataDownloader {
    private static final String BASE_URL = "https://www.reddit.com/r/all/top.json";
    private static final String TAG = "DataDownloader";

    private OnDataUpdateCallback mOnDataUpdateCallback;

    public DataDownloader(OnDataUpdateCallback onDataUpdateCallback) {
        mOnDataUpdateCallback = onDataUpdateCallback;
    }

    public void getPosts() {
        new JSONLoadTask().execute("");
    }

    private List<Post> getPostsFromJSON(String json) {
        List<Post> posts = new ArrayList<>();
        try {

            JSONObject body = new JSONObject(json);
            JSONObject dataScope = body.getJSONObject(DATA);
            JSONArray children = dataScope.getJSONArray(CHILDREN);
            for (int i = 0; i < children.length(); i++) {
                JSONObject linkDataScope = children.getJSONObject(i).getJSONObject(DATA);
                Post post = new Post();
                post.setAuthor(linkDataScope.getString(SUBREDDIT_NAME_PREFIXED));
                post.setTitle(linkDataScope.getString(TITLE));
                post.setPostTime(linkDataScope.getLong(CREATED_UTC));
                post.setCommentsCount(linkDataScope.getInt(NUM_COMMENTS));
                post.setThumbnailUrl(linkDataScope.getString(THUMBNAIL));
                if (post.hasImage()){
                    post.setThumbnailHeight(linkDataScope.getInt(THUMBNAIL_HEIGHT));
                    post.setThumbnailWidth(linkDataScope.getInt(THUMBNAIL_WIDTH));
                }
                posts.add(post);
            }
            return posts;
        } catch (JSONException e) {
            Log.i(TAG, "getPostsFromJSON: error - " + e.getMessage());
            return null;
        }
    }

    private class JSONLoadTask extends AsyncTask<String, Void, List<Post>> {

        @Override
        protected List<Post> doInBackground(String... strings) {
            String s = "";
            try {
                s = new String(getUrlBytes(BASE_URL));
                Log.i(TAG, "doInBackground: " + s);
            } catch (IOException e) {
                Log.i(TAG, "doInBackground: error - " + e.getMessage());
                return null;
            }
            return getPostsFromJSON(s);
        }

        @Override
        protected void onPostExecute(List<Post> posts) {
            mOnDataUpdateCallback.onDataUpdate(posts);
        }
    }

    public byte[] getUrlBytes(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Log.i(TAG, "getUrlBytes: " + connection.getResponseCode());
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                mOnDataUpdateCallback.onConnectionFailed();
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

}
