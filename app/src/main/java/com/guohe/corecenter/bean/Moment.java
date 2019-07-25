package com.guohe.corecenter.bean;

public class Moment {
    private String id;
    private String imageUrl;
    private String content;
    private String accountImageUrl;
    private String accountNickName;
    private String favorite;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAccountImageUrl() {
        return accountImageUrl;
    }

    public void setAccountImageUrl(String accountImageUrl) {
        this.accountImageUrl = accountImageUrl;
    }

    public String getAccountNickName() {
        return accountNickName;
    }

    public void setAccountNickName(String accountNickName) {
        this.accountNickName = accountNickName;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }
}
