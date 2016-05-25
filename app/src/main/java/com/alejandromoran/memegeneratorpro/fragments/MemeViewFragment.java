package com.alejandromoran.memegeneratorpro.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.alejandromoran.memegeneratorpro.R;
import com.alejandromoran.memegeneratorpro.entities.Favourite;
import com.alejandromoran.memegeneratorpro.entities.Memes;
import com.alejandromoran.memegeneratorpro.utils.Meme;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MemeViewFragment extends Fragment {

    private Memes memes;
    private int offset;

    @BindView(R.id.memeView)
    ImageView imageView;

    public MemeViewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_meme_view, container, false);
        ButterKnife.bind(this, rootView);
        this.offset = 0;
        showMeme();

       /* int heightPixels = AdSize.SMART_BANNER.getHeightInPixels(getActivity());
        LinearLayout actionBar = (LinearLayout) rootView.findViewById(R.id.memeActionBar);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) actionBar.getLayoutParams();
        layoutParams.setMargins(0,0,0,heightPixels);*/

        return rootView;

    }

    @OnClick(R.id.share)
    public void shareMeme() {
        Meme.share(getActivity(), memes.getImageBitmap());
    }

    @OnClick(R.id.favourite)
    public void markAsFavourite() {

        Favourite favourite = new Favourite();
        favourite.setMemeId(memes.getObjectId());
        favourite.setUserId(Backendless.UserService.CurrentUser().getUserId());;
        Backendless.Data.of(Favourite.class).save(favourite, new AsyncCallback<Favourite>() {
            public void handleResponse( Favourite favourite )
            {
                Toast.makeText(getContext(), "Marked as favourite", Toast.LENGTH_SHORT).show();
                showMeme();
            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                Log.d("DEBUG", "fault!!" + fault.toString());
            }
        });

    }

    @OnClick({R.id.thumbsDown, R.id.thumbsUp})
    public void thumbs(View view) {
        showMeme();
    }

    public void showMeme() {

        Backendless.Persistence.of(Memes.class).find(new AsyncCallback<BackendlessCollection<Memes>>() {
            public void handleResponse( BackendlessCollection<Memes> pagesCollection )
            {
                pagesCollection.getPage(1,offset++,new AsyncCallback<BackendlessCollection<Memes>>() {
                    public void handleResponse( BackendlessCollection<Memes> memesCollection )
                    {
                        List<Memes> memesList = memesCollection.getData();
                        if (memesList.size() > 0) {
                            final Memes memes = memesList.get(0);
                            MemeViewFragment.this.memes = memes;
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

                            Picasso.with(getActivity()).load(memes.getImageUrl()).into(target);


                        }
                    }
                    @Override
                    public void handleFault( BackendlessFault fault )
                    {
                        Log.d("DEBUG", "fault!!" + fault.toString());
                    }
                });
            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                Log.d("DEBUG", "fault!!" + fault.toString());
            }
        });
    }

}
