package com.alejandromoran.memegeneratorpro.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.content.Context;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.content.res.Resources.Theme;
import android.widget.TextView;

import com.alejandromoran.memegeneratorpro.BuildConfig;
import com.alejandromoran.memegeneratorpro.R;
import com.alejandromoran.memegeneratorpro.entities.Memes;
import com.alejandromoran.memegeneratorpro.fragments.MemeGeneratorFragment;
import com.alejandromoran.memegeneratorpro.fragments.MemeListFragment;
import com.alejandromoran.memegeneratorpro.fragments.MemeViewFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity2 extends AppCompatActivity implements MemeListFragment.OnListFragmentInteractionListener {

    @BindView(R.id.adView)
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        if (BuildConfig.FLAVOR.equals("classic")) {
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("EF4399DF4740B660198FF48DBDB7AFFC").build();
            mAdView.loadAd(adRequest);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.logo);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Setup spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        "Public Memes",
                        "Create",
                        "My Gallery"
                }));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
                    default:
                        fragment = new MemeViewFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mainx, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_logout) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // user is now signed out
                            startActivity(new Intent(MainActivity2.this, LoginActivity.class));
                            finish();
                        }
                    });
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Memes item) {

    }


    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }

}
