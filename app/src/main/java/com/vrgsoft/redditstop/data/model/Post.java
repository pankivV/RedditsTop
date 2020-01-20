package com.vrgsoft.redditstop.data.model;

import android.text.TextUtils;

import static com.vrgsoft.redditstop.data.RedditJSONKeyNames.DEFAULT;
import static com.vrgsoft.redditstop.data.RedditJSONKeyNames.IMAGE;
import static com.vrgsoft.redditstop.data.RedditJSONKeyNames.SELF;

public class Post {

    private String mAuthor;
    private long mPostTime;
    private String mTitle;
    private String mImageUrl;
    private String mImageUrlBig;
    private int mCommentsCount;
    private String mThumbnailUrl;
    private String mHighResImageUrl;
    private String mAfter;
    private int mThumbnailHeight;
    private int mThumbnailWidth;
    private boolean mHasImage;

    public String getAfter() {
        return mAfter;
    }

    public void setAfter(String after) {
        mAfter = after;
    }

    public boolean hasImage() {
        return mHasImage;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        mThumbnailUrl = thumbnailUrl;
        mHasImage = thumbnailUrl.contains(".jpg");
    }

    public String getHighResImageUrl() {
        return mHighResImageUrl;
    }

    public void setHighResImageUrl(String highResImageUrl) {
        mHighResImageUrl = highResImageUrl;
    }

    public int getThumbnailHeight() {
        return mThumbnailHeight;
    }

    public void setThumbnailHeight(int thumbnailHeight) {
        mThumbnailHeight = thumbnailHeight;
    }

    public int getThumbnailWidth() {
        return mThumbnailWidth;
    }

    public void setThumbnailWidth(int thumbnailWidth) {
        mThumbnailWidth = thumbnailWidth;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public long getPostTime() {
        return mPostTime;
    }

    public void setPostTime(long postTime) {
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
