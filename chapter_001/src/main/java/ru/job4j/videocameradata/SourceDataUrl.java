package ru.job4j.videocameradata;

class SourceDataUrl {
    private String urlType;
    private String videoUrl;

    public void setUrlType(final String urlType) {
        this.urlType = urlType;
    }

    public void setVideoUrl(final String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getUrlType() {
        return urlType;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
