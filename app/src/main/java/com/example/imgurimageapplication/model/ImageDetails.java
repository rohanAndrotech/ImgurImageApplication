package com.example.imgurimageapplication.model;

import java.util.List;

/**
 * ImageDetails model
 */
public class ImageDetails {
    private List<String> imageLink;
    private List<String> imageTitle;

    public ImageDetails(List<String> imageLink, List<String> imageTitle) {
        this.imageLink = imageLink;
        this.imageTitle = imageTitle;
    }

    public List<String> getImageLink() {
        return imageLink;
    }

    public void setImageLink(List<String> imageLink) {
        this.imageLink = imageLink;
    }

    public List<String> getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(List<String> imageTitle) {
        this.imageTitle = imageTitle;
    }

}
