package com.vrgsoft.redditstop.data.model;

public class Post {

    private String mAuthor;
    private long mPostTime;
    private String mTitle;
    private int mCommentsCount;
    private String mThumbnailUrl;
    private String mUrl;
    private String mAfter;
    private String mVideoUrl;
    private int mThumbnailHeight;
    private int mThumbnailWidth;
    private boolean mHasImage;
    private boolean mHasAnimatedImage;
    private boolean isVideo;

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    public String getAfter() {
        return mAfter;
    }

    public void setAfter(String after) {
        mAfter = after;
    }

    public boolean hasImage() {
        return mHasImage;
    }

    public boolean hasAnimatedImage() {
        return mHasAnimatedImage;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        if (thumbnailUrl.contains(".jpg")) {
            mThumbnailUrl = thumbnailUrl;
        }
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
        mHasImage = url.contains(".jpg");
        mHasAnimatedImage = url.contains(".gif");
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

    public int getCommentsCount() {
        return mCommentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        mCommentsCount = commentsCount;
    }
}
