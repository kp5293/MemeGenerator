package com.alejandromoran.memegeneratorpro.entities;

import android.graphics.Bitmap;

/**
 * Created by Ale on 23/01/2016.
 */

public class Meme {


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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
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
    private Bitmap image;



}
