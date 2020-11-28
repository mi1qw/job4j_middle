package ru.job4j.videocameradata;

class DataURLCameras {
    private int id;
    private String sourceDataUrl;
    private String tokenDataUrl;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getSourceDataUrl() {
        return sourceDataUrl;
    }

    public void setSourceDataUrl(final String sourceDataUrl) {
        this.sourceDataUrl = sourceDataUrl;
    }

    public String getTokenDataUrl() {
        return tokenDataUrl;
    }

    public void setTokenDataUrl(final String tokenDataUrl) {
        this.tokenDataUrl = tokenDataUrl;
    }
}
