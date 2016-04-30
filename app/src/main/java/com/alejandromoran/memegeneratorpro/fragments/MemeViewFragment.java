package com.alejandromoran.memegeneratorpro.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.alejandromoran.memegeneratorpro.R;
import com.alejandromoran.memegeneratorpro.entities.Meme;
import com.alejandromoran.memegeneratorpro.utils.BackendlessDBUtil;
import com.backendless.Backendless;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MemeViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MemeViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemeViewFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private int skip;
    private Meme meme;

    public MemeViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MemeViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MemeViewFragment newInstance(String param1, String param2) {
        MemeViewFragment fragment = new MemeViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void shareMeme() {

       /* Meme meme = ParseDBUtil.getLastMeme(this.skip);
        String pathofBmp = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), meme.getMemeImageBitmap(), "meme", null);
        Uri bmpUri = Uri.parse(pathofBmp);
        final Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/png");
        startActivity(shareIntent);*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_meme_view, container, false);
        this.skip = 0;

        showMeme(rootView);

       /* int heightPixels = AdSize.SMART_BANNER.getHeightInPixels(getActivity());
        LinearLayout actionBar = (LinearLayout) rootView.findViewById(R.id.memeActionBar);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) actionBar.getLayoutParams();
        layoutParams.setMargins(0,0,0,heightPixels);*/

        ImageButton shareMeme = (ImageButton) rootView.findViewById(R.id.share);
        shareMeme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareMeme();
            }
        });

        ImageButton favourite = (ImageButton) rootView.findViewById(R.id.favourite);
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markAsFavourite();
            }
        });

        ImageButton thumbsUp = (ImageButton) rootView.findViewById(R.id.thumbsUp);
        thumbsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip++;
                showMeme(rootView);
            }
        });

        ImageButton thumbsDown = (ImageButton) rootView.findViewById(R.id.thumbsDown);
        thumbsDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip++;
                showMeme(rootView);
            }
        });

        return rootView;

    }

    public void markAsFavourite() {
        BackendlessDBUtil.markAsFavourite(meme.getObjectId(), Backendless.UserService.CurrentUser().getObjectId());
        Toast.makeText(getContext(), "Marked as favourite", Toast.LENGTH_SHORT).show();
    }

    public void showMeme(View view) {

        Meme meme = BackendlessDBUtil.getLastMeme(skip);
        if(meme != null) {
            this.meme = meme;
            ImageView imageView = (ImageView) view.findViewById(R.id.memeView);
            imageView.setImageBitmap(meme.getImage());
        }

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
