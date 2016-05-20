package com.alejandromoran.memegeneratorpro.activities;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import com.alejandromoran.memegeneratorpro.R;
import com.alejandromoran.memegeneratorpro.fragments.MemeGeneratorFragment;
import com.alejandromoran.memegeneratorpro.fragments.MemeListFragment;
import com.alejandromoran.memegeneratorpro.fragments.MemeViewFragment;
import com.alejandromoran.memegeneratorpro.utils.Meme;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MemeViewFragment.OnFragmentInteractionListener, MemeListFragment.OnListFragmentInteractionListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;


    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.adView)
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this.getApplicationContext());
        ButterKnife.bind(this);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("EF4399DF4740B660198FF48DBDB7AFFC").build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Meme item) {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private Context context;

        public SectionsPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }



        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new MemeViewFragment();
                    break;
                case 1:
                    fragment = new MemeGeneratorFragment();
                    break;
                case 2:
                    fragment = new MemeListFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return context.getString(R.string.publicMemes);
                case 1:
                    return context.getString(R.string.createMeme);
                case 2:
                    return context.getString(R.string.myMemes);
            }
            return null;
        }
    }
}
