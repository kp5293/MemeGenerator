package com.alejandromoran.memegeneratorpro.entities;

/**
 * Created by alejandromoran on 20/05/16.
 */
public class Memes {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopText() {
        return topText;
    }

    public void setTopText(String topText) {
        this.topText = topText;
    }

    public String getBottomText() {
        return bottomText;
    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    private String objectId;
    private String name;
    private String topText;
    private String bottomText;
    private String imageUrl;

}
