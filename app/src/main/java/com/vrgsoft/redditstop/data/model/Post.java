package com.vrgsoft.redditstop.data.model;

public class Post {

    private String mAuthor;
    private String mPostTime;
    private String mTitle;
    private String mImageUrl;
    private String mImageUrlBig;
    private int mCommentsCount;

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getPostTime() {
        return mPostTime;
    }

    public void setPostTime(String postTime) {
        mPostTime = postTime;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getImageUrlBig() {
        return mImageUrlBig;
    }

    public void setImageUrlBig(String imageUrlBig) {
        mImageUrlBig = imageUrlBig;
    }

    public int getCommentsCount() {
        return mCommentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        mCommentsCount = commentsCount;
    }
}
