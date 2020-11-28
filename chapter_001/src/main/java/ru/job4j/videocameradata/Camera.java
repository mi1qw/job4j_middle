package ru.job4j.videocameradata;

import java.util.Objects;

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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Camera camera = (Camera) o;
        return id == camera.id
                && ttl == camera.ttl && urlType.equals(camera.urlType)
                && videoUrl.equals(camera.videoUrl) && value.equals(camera.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, urlType, videoUrl, value, ttl);
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
