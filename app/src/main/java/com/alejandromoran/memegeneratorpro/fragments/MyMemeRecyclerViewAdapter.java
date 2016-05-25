package com.alejandromoran.memegeneratorpro.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.alejandromoran.memegeneratorpro.R;
import com.alejandromoran.memegeneratorpro.activities.MemeViewActivity;
import com.alejandromoran.memegeneratorpro.entities.Memes;
import com.alejandromoran.memegeneratorpro.fragments.MemeListFragment.OnListFragmentInteractionListener;
import com.alejandromoran.memegeneratorpro.utils.Meme;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;


public class MyMemeRecyclerViewAdapter extends RecyclerView.Adapter<MyMemeRecyclerViewAdapter.ViewHolder> {

    private final List<Memes> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyMemeRecyclerViewAdapter(List<Memes> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_memelist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mThumb.setTag(mValues.get(position).getObjectId());
        holder.mContentView.setText(mValues.get(position).getName());

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.mThumb.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(holder.mView.getContext()).load(mValues.get(position).getImageUrl()).into(target);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
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
        public Memes mItem;

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
