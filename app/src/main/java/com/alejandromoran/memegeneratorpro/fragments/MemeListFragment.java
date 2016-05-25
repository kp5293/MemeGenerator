package com.alejandromoran.memegeneratorpro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alejandromoran.memegeneratorpro.R;
import com.alejandromoran.memegeneratorpro.entities.Memes;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MemeListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView rv;
    private List<String> ownCategories;

    @BindView(R.id.list)
    RecyclerView recyclerView;

    public MemeListFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        getMyMemes();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_memelist_list, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        getMyMemes();
        this.rv = recyclerView;
        ownCategories = new ArrayList<>();
        ownCategories.add(getString(R.string.myGallery));
        ownCategories.add(getString(R.string.myFavourites));

        return view;

    }

    @OnClick({R.id.leftOwn, R.id.rightOwn})
    public void onClick(View view) {
        loadCategory();
    }

    public void getMyMemes() {

        String whereClause = String.format("userId='%s'", Backendless.UserService.CurrentUser().getObjectId());
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
        AsyncCallback<BackendlessCollection<Memes>> callback = new AsyncCallback<BackendlessCollection<Memes>>() {
            public void handleResponse( BackendlessCollection<Memes> memesCollection )
            {
                List<Memes> memesList = memesCollection.getCurrentPage();
                recyclerView.setAdapter(new MyMemeRecyclerViewAdapter(memesList, mListener));
            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                Log.d("DEBUG", "fault!!" + fault.toString());
            }
        };
        Backendless.Persistence.of(Memes.class).find(dataQuery, callback);

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

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Memes item);
    }
}
