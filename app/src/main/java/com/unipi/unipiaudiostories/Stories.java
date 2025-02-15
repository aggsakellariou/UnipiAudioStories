package com.unipi.unipiaudiostories;

import java.util.List;

public class Stories {

    private int id;
    private String title;
    private String author;
    private String year;
    private String description;
    private String imageURL;
    private List<String> imageUrls;
    private List<String> imageTexts;

    public Stories(String author, int id, String title, String year, String description, String imageURL, List<String> imageUrls, List<String> imageTexts) {
        this.author = author;
        this.id = id;
        this.title = title;
        this.year = year;
        this.description = description;
        this.imageURL = imageURL;
        this.imageUrls = imageUrls;
        this.imageTexts = imageTexts;
    }

    public Stories() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<String> getImageTexts() {
        return imageTexts;
    }

    public void setImageTexts(List<String> imageTexts) {
        this.imageTexts = imageTexts;
    }
}
