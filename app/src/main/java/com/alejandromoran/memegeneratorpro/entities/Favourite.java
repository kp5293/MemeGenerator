package com.alejandromoran.memegeneratorpro.entities;

/**
 * Created by Ale on 23/01/2016.
 */

public class Favourite {

    private String userId;

    public String getMemeId() {
        return memeId;
    }

    public void setMemeId(String memeId) {
        this.memeId = memeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String memeId;

}
