package ru.job4j.videocameradata;

class Camera {
    private int id;
    private String urlType;
    private String videoUrl;
    private String value;
    private int ttl;

    Camera(final int id, final SourceDataUrl source, final TokenDataUrl token) {
        this.id = id;
        this.urlType = source.getUrlType();
        this.videoUrl = source.getVideoUrl();
        this.value = token.getValue();
        this.ttl = token.getTtl();
    }

    @Override
    public String toString() {
        return "Camera{"
                + "id=" + id
                + ", urlType='" + urlType + '\''
                + ", videoUrl='" + videoUrl + '\''
                + ", value='" + value + '\''
                + ", ttl=" + ttl + '}';
    }
}
