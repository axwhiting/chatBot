package com.techelevator.model;

public class Meme {

    private String postLink;
    private String subreddit;
    private String url;
    private String title;
    private boolean nsfw;
    private boolean spoiler;
    private String author;
    private String ups;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostLink() {
        return postLink;
    }

    public void setPostLink(String postLink) {
        this.postLink = postLink;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isNsfw() {
        return nsfw;
    }

    public void setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
    }

    public boolean isSpoiler() {
        return spoiler;
    }

    public void setSpoiler(boolean spoiler) {
        this.spoiler = spoiler;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUps() {
        return ups;
    }

    public void setUps(String ups) {
        this.ups = ups;
    }
}
