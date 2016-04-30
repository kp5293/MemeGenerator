package com.alejandromoran.memegeneratorpro.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Created by Ale on 01/02/2016.
 */
public class Quote extends Meme {


    @Override
    protected int getMemeWidth() {
        int memeWidth;
        int imageWidth = this.image.getWidth();

        if (getMajorSentenceWidth() > imageWidth) {
            memeWidth = (int) (imageWidth + getMajorSentenceWidth() + 20);
        }
        else {
            memeWidth = imageWidth*2;
        }

        return memeWidth;
    }

    @Override
    public void generateImage() {
        if (this.image != null) {

            Bitmap dest = Bitmap.createBitmap(getMemeWidth(), this.image.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas cs = new Canvas(dest);
            cs.drawColor(Color.BLACK);
            cs.drawBitmap(this.image, 0f, 0f, null);
            cs.drawText(topText, getSentenceXPosition(getTopTextWidth()), getTopTextYPosition(), fillPaint);
            cs.drawText(bottomText, getSentenceXPosition(getBottomTextWidth()), getBottomTextYPosition(), fillPaint);
            this.meme = dest;

        }
    }

    @Override
    protected float getTopTextYPosition() {
        return (this.image.getHeight()/2)-30;
    }

    @Override
    protected float getBottomTextYPosition() {
        return (this.image.getHeight()/2)+30;
    }


    private float getMajorSentenceWidth() {

        float majorWidth;

        if (getTopTextWidth() > getBottomTextWidth()) {
            majorWidth = getTopTextWidth();
        }
        else {
            majorWidth = getBottomTextWidth();
        }

        return majorWidth;

    }

    private float getSentenceXPosition(float sentenceWidth) {

        float position = getMemeWidth() - (getMemeWidth() / 4) - (getMajorSentenceWidth() / 2);

        if (sentenceWidth >= getMajorSentenceWidth()) {
            if (getMajorSentenceWidth() > this.image.getWidth()) {
                position = this.image.getWidth() + 10;
            }
        }
        else {
            position = this.image.getWidth() + (getMajorSentenceWidth()/2) - (sentenceWidth/2);
        }

        return position;

    }

}
