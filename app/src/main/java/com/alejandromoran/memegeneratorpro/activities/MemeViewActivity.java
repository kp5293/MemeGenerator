package com.alejandromoran.memegeneratorpro.activities;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.alejandromoran.memegeneratorpro.R;
import com.alejandromoran.memegeneratorpro.entities.Meme;
import com.alejandromoran.memegeneratorpro.utils.BackendlessDBUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MemeViewActivity extends AppCompatActivity {

    private Meme meme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_view);

        Intent intent = getIntent();
        String objectId = intent.getStringExtra("objectId");
        ImageButton shareMeme = (ImageButton) findViewById(R.id.shareMeme);
        ImageButton deleteMeme = (ImageButton) findViewById(R.id.deleteMeme);
        meme = BackendlessDBUtil.getMemeById(objectId);

        ImageView imageView = (ImageView) findViewById(R.id.memeView);
        imageView.setImageBitmap(meme.getImage());

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("EF4399DF4740B660198FF48DBDB7AFFC").build();
        mAdView.loadAd(adRequest);

        shareMeme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareMeme();
            }
        });

        deleteMeme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMeme();
            }
        });

    }

    private void shareMeme() {

      /*  String pathofBmp = MediaStore.Images.Media.insertImage(this.getContentResolver(), meme.getMemeImageBitmap(), "meme", null);
        Uri bmpUri = Uri.parse(pathofBmp);
        final Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/png");
        startActivity(shareIntent);*/

    }

    public void deleteMeme() {
        BackendlessDBUtil.deleteMeme(meme.getObjectId());
        this.finish();
    }

}
