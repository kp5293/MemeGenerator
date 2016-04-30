package com.alejandromoran.memegeneratorpro.fragments;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alejandromoran.memegeneratorpro.R;
import com.alejandromoran.memegeneratorpro.activities.MemeViewActivity;
import com.alejandromoran.memegeneratorpro.entities.Meme;
import com.alejandromoran.memegeneratorpro.fragments.MemeListFragment.OnListFragmentInteractionListener;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Meme} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMemeRecyclerViewAdapter extends RecyclerView.Adapter<MyMemeRecyclerViewAdapter.ViewHolder> {

    private final List<Meme> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyMemeRecyclerViewAdapter(List<Meme> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_memelist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
       /* holder.mThumb.setImageBitmap(mValues.get(position).getMemeImageBitmap());
        holder.mThumb.setTag(mValues.get(position).getObjectId());
        holder.mContentView.setText(mValues.get(position).getMemeName());*/

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final ImageView mThumb;
        public final TextView mContentView;
        public Meme mItem;

        public ViewHolder(View view) {

            super(view);
            mView = view;
            mThumb = (ImageView) view.findViewById(R.id.memeThumb);
            mContentView = (TextView) view.findViewById(R.id.content);
            mThumb.setOnClickListener(this);
            mContentView.setOnClickListener(this);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), MemeViewActivity.class);
            intent.putExtra("objectId", mThumb.getTag().toString());
            v.getContext().startActivity(intent);
        }
    }
}
