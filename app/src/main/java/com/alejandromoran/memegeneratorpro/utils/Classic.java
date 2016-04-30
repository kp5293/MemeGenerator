package com.alejandromoran.memegeneratorpro.utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;

/**
 * Created by Ale on 01/02/2016.
 */
public class Classic extends Meme {

    private TextPaint strokePaint;

    public Classic(AssetManager assetManager) {
        Typeface typeFace = Typeface.createFromAsset(assetManager, "impact.ttf");
        this.strokePaint = new TextPaint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(1);
        strokePaint.setColor(Color.BLACK);
        strokePaint.setAntiAlias(true);
        strokePaint.setTypeface(typeFace);
        fillPaint.setTypeface(typeFace);
    }

    @Override
    protected int getMemeWidth() {
        return this.image.getWidth();
    }

    @Override
    public void generateImage() {

        if (this.image != null) {

            Bitmap dest = Bitmap.createBitmap(getMemeWidth(), this.image.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas cs = new Canvas(dest);
            cs.drawColor(Color.BLACK);
            cs.drawBitmap(this.image, 0f, 0f, null);
            fillPaint.setTextSize(getBestTextSize(this.topText));
            strokePaint.setTextSize(getBestTextSize(this.topText));
            cs.drawText(topText, (this.image.getWidth() / 2) - (getTopTextWidth() / 2), getTopTextYPosition(), fillPaint);
            cs.drawText(topText, (this.image.getWidth() / 2) - (getTopTextWidth() / 2), getTopTextYPosition(), strokePaint);
            fillPaint.setTextSize(getBestTextSize(this.bottomText));
            strokePaint.setTextSize(getBestTextSize(this.bottomText));
            cs.drawText(bottomText, (this.image.getWidth() / 2) - (getBottomTextWidth() / 2), getBottomTextYPosition(), fillPaint);
            cs.drawText(bottomText, (this.image.getWidth() / 2) - (getBottomTextWidth() / 2), getBottomTextYPosition(), strokePaint);

            this.meme = dest;

        }

    }

    private int getBestTextSize(String text) {

        int bestSize = (this.image.getHeight()/4);
        Paint paint = new Paint();
        paint.setTextSize(bestSize);

        while (paint.measureText(text) > this.image.getWidth()) {
            bestSize--;
            paint.setTextSize(bestSize);
        }

        bestSize = bestSize - 2;

        return bestSize;

    }

    @Override
    protected float getTopTextYPosition() {
        return (this.image.getHeight()/8) + (fillPaint.getTextSize()/2);
    }

    @Override
    protected float getBottomTextYPosition() {
        return this.image.getHeight() - (this.image.getHeight()/8) + (fillPaint.getTextSize()/2);
    }
}
