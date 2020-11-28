package ru.job4j.videocameradata;

class TokenDataUrl {
    private String value;
    private int ttl;

    public void setValue(final String value) {
        this.value = value;
    }

    public void setTtl(final int ttl) {
        this.ttl = ttl;
    }

    public String getValue() {
        return value;
    }

    public int getTtl() {
        return ttl;
    }
}
