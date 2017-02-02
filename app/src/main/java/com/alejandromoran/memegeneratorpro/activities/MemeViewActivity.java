package com.alejandromoran.memegeneratorpro.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.alejandromoran.memegeneratorpro.BuildConfig;
import com.alejandromoran.memegeneratorpro.R;
import com.alejandromoran.memegeneratorpro.entities.Memes;
import com.alejandromoran.memegeneratorpro.utils.Meme;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MemeViewActivity extends AppCompatActivity {

    private Memes memes;

    @BindView(R.id.memeView)
    ImageView imageView;

    @BindView(R.id.adView)
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_view);
        ButterKnife.bind(this);
        showMeme();
        Log.d("DEBUG", "FLAVOR: " + BuildConfig.FLAVOR);
        if (BuildConfig.FLAVOR.equals("classic")) {
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("EF4399DF4740B660198FF48DBDB7AFFC").build();
            mAdView.loadAd(adRequest);
        }

    }

    public void showMeme() {
        Intent intent = getIntent();
        String objectId = intent.getStringExtra("objectId");

       /* Backendless.Persistence.of(Memes.class).findById(objectId, new AsyncCallback<Memes>() {
            @Override
            public void handleResponse(Memes response) {
                memes = response;
                Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        imageView.setImageBitmap(bitmap);
                        memes.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };

                Picasso.with(MemeViewActivity.this).load(memes.getImageUrl()).into(target);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });*/
    }

    @OnClick(R.id.shareMeme)
    public void shareMeme(View view) {
        Meme.share(this, memes.getImageBitmap());
    }

    @OnClick(R.id.deleteMeme)
    public void deleteMeme() {
      /*  Backendless.Persistence.of(Memes.class).findById(memes.getObjectId(), new AsyncCallback<Memes>() {
            @Override
            public void handleResponse(Memes response) {
                Backendless.Persistence.of(Memes.class).remove(response, new AsyncCallback<Long>() {
                    @Override
                    public void handleResponse(Long response) {
                        MemeViewActivity.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        MemeViewActivity.this.finish();
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                MemeViewActivity.this.finish();
            }
        });*/
    }

}
