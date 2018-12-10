package com.example.android.finalproject;

public final class FavCat {
    String favId;
    String url;
    String id;

    public FavCat(String favId, String url, String id) {
        this.favId = favId;
        this.url = url;
        this.id = id;
    }

    public String getFavId() {
        return favId;
    }

    public void setFavId(String favId) {
        this.favId = favId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
