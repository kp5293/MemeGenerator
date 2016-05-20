package com.alejandromoran.memegeneratorpro.utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.Log;

/**
 * Created by Ale on 22/01/2016.
 */
public abstract class Meme {

    private int textSize;
    protected Bitmap image;
    protected Bitmap meme;
    protected String topText;
    protected String bottomText;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    protected String objectId;
    protected TextPaint fillPaint;

    public Meme() {
        this.topText = "";
        this.bottomText = "";
        this.fillPaint = new TextPaint();
        fillPaint.setColor(Color.WHITE);
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setAntiAlias(true);
    }


    public Bitmap getImage() {
        return this.image;
    }

    public void setTopText(String topText) {
        this.topText = topText;
    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
    }

    protected float getTopTextWidth() {
        return fillPaint.measureText(this.topText);
    }

    protected float getBottomTextWidth() {
        return fillPaint.measureText(this.bottomText);
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    protected abstract int getMemeWidth();

    public abstract void generateImage();

    public Bitmap getMeme() {
        return this.meme;
    }

    public String getTopText() {
        return this.topText;
    }

    public String getBottomText() {
        return this.bottomText;
    }

    protected abstract float getTopTextYPosition();

    protected abstract float getBottomTextYPosition();

}
