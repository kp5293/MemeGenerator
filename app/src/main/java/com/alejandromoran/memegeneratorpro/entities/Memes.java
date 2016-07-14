package com.alejandromoran.memegeneratorpro.entities;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by alejandromoran on 20/05/16.
 */
public class Memes {

    private String userId;
    private String objectId;
    private String name;
    private String topText;
    private String bottomText;
    private String imageUrl;
    private boolean isApproved;
    private Bitmap imageBitmap;
    private boolean isPublic;

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

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public boolean isApproved() {
        return this.isApproved;
    }

    public boolean isPublic() {

        Log.d("DEBUG", "isPublic inside Memes? -> " + this.isPublic);
        return this.isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
