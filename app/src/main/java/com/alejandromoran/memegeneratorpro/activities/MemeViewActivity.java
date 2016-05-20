package com.alejandromoran.memegeneratorpro.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.alejandromoran.memegeneratorpro.R;
import com.alejandromoran.memegeneratorpro.utils.BackendlessDBUtil;
import com.alejandromoran.memegeneratorpro.utils.Meme;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MemeViewActivity extends AppCompatActivity {

    private Meme memes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_view);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String objectId = intent.getStringExtra("objectId");

        memes = BackendlessDBUtil.getMemeById(objectId);

        ImageView imageView = (ImageView) findViewById(R.id.memeView);
        imageView.setImageBitmap(memes.getImage());

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("EF4399DF4740B660198FF48DBDB7AFFC").build();
        mAdView.loadAd(adRequest);


    }

    @OnClick(R.id.shareMeme)
    public void shareMeme(View view) {
        /*
        String pathofBmp = MediaStore.Images.Media.insertImage(this.getContentResolver(), memes.getMemeImageBitmap(), "memes", null);
        Uri bmpUri = Uri.parse(pathofBmp);
        final Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/png");
        startActivity(shareIntent);
        */
    }

    @OnClick(R.id.deleteMeme)
    public void deleteMeme() {
        BackendlessDBUtil.deleteMeme(memes.getObjectId());
        this.finish();
    }

}
