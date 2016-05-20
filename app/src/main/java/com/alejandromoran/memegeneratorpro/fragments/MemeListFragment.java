package com.alejandromoran.memegeneratorpro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.alejandromoran.memegeneratorpro.R;
import com.alejandromoran.memegeneratorpro.utils.Meme;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MemeListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView rv;
    private List<String> ownCategories;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MemeListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MemeListFragment newInstance(int columnCount) {
        MemeListFragment fragment = new MemeListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
       // rv.setAdapter(new MyMemeRecyclerViewAdapter(ParseDBUtil.getMyMemes(), mListener));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_memelist_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
       // recyclerView.setAdapter(new MyMemeRecyclerViewAdapter(ParseDBUtil.getMyMemes(), mListener));
        this.rv = recyclerView;

        ownCategories = new ArrayList<>();
        ownCategories.add(getString(R.string.myGallery));
        ownCategories.add(getString(R.string.myFavourites));


        ImageButton leftOwn = (ImageButton) view.findViewById(R.id.leftOwn);
        ImageButton rightOwn = (ImageButton) view.findViewById(R.id.rightOwn);

        leftOwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCategory();
            }
        });

        rightOwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCategory();
            }
        });

        return view;

    }

    public void loadCategory() {
        /*TextView ownCategories = (TextView) getView().findViewById(R.id.ownCategories);
        String currentText = ownCategories.getText().toString();
        if (currentText.equals(getString(R.string.myGallery))) {
            ownCategories.setText(getString(R.string.myFavourites));
            this.rv.setAdapter(new MyMemeRecyclerViewAdapter(ParseDBUtil.getMyFavouriteMemes(), mListener));
        }
        else {
            ownCategories.setText(getString(R.string.myGallery));
            this.rv.setAdapter(new MyMemeRecyclerViewAdapter(ParseDBUtil.getMyMemes(), mListener));
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Meme item);
    }
}
