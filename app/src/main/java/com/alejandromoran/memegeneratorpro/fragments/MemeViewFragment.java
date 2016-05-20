package com.alejandromoran.memegeneratorpro.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.alejandromoran.memegeneratorpro.R;
import com.alejandromoran.memegeneratorpro.entities.Memes;
import com.alejandromoran.memegeneratorpro.utils.BackendlessDBUtil;
import com.alejandromoran.memegeneratorpro.utils.Meme;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class MemeViewFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private int skip;
    private Meme memes;

    public MemeViewFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_meme_view, container, false);
        ButterKnife.bind(this, rootView);
        this.skip = 0;

        showMeme(rootView);

       /* int heightPixels = AdSize.SMART_BANNER.getHeightInPixels(getActivity());
        LinearLayout actionBar = (LinearLayout) rootView.findViewById(R.id.memeActionBar);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) actionBar.getLayoutParams();
        layoutParams.setMargins(0,0,0,heightPixels);*/

        return rootView;

    }

    @OnClick(R.id.share)
    public void shareMeme() {

       /* Memes memes = ParseDBUtil.getLastMeme(this.skip);
        String pathofBmp = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), memes.getMemeImageBitmap(), "memes", null);
        Uri bmpUri = Uri.parse(pathofBmp);
        final Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/png");
        startActivity(shareIntent);*/

    }

    @OnClick(R.id.favourite)
    public void markAsFavourite() {
        BackendlessDBUtil.markAsFavourite(memes.getObjectId(), Backendless.UserService.CurrentUser().getObjectId());
        Toast.makeText(getContext(), "Marked as favourite", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.thumbsDown, R.id.thumbsUp})
    public void thumbs(View view) {
        skip++;
        showMeme(getView());
    }

    public void showMeme(View view) {
        Backendless.Persistence.of( Memes.class).find(new AsyncCallback<BackendlessCollection<Memes>>(){
            @Override
            public void handleResponse( BackendlessCollection<Memes> foundMemes )
            {
                // all Contact instances have been found
                Log.d("BACKEND", "DATA FOUDN!!! " + String.valueOf(foundMemes.getCurrentPage().size()));
                //this.memes = memes;
                //ImageView imageView = (ImageView) view.findViewById(R.id.memeView);
                //imageView.setImageBitmap(memes.getImage());
            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                Log.d("BACKEND", " " + fault.getMessage() + " " + fault.getCode());
            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
