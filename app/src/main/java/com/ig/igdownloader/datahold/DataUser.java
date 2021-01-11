package com.ig.igdownloader.datahold;

public class DataUser {
    String singleVideoLink;
    String thumbnailURL;
    String videoURL;

    public DataUser(String thumbnailURL, String videoURL) {  // for list of videos and thumbnails.
        this.thumbnailURL = thumbnailURL;
        this.videoURL = videoURL;
    }

    public String getVideoUrl() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void data(String singleVideoLink) {
        this.singleVideoLink = singleVideoLink;
    }

    public String data() {
        return singleVideoLink;  // for single video url
    }
}
