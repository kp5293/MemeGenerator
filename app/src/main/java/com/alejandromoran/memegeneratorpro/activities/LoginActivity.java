package com.alejandromoran.memegeneratorpro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.alejandromoran.memegeneratorpro.R;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    AsyncCallback<BackendlessUser> loginCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Backendless.initApp(this, getString(R.string.backendles_app_id), getString(R.string.backendles_app_secret),  getString(R.string.backendles_app_version));

        loginCallback = new AsyncCallback<BackendlessUser>()
        {
            @Override
            public void handleResponse( BackendlessUser loggedInUser )
            {
                Backendless.UserService.setCurrentUser(loggedInUser);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.finish();
                startActivity(intent);
            }

            @Override
            public void handleFault( BackendlessFault fault )
            {
                Toast.makeText(LoginActivity.this, getString(R.string.loginError), Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.facebookLogin)
    public void facebookLogin(View view) {
        Map<String, String> facebookFieldMappings = new HashMap<String, String>();
        facebookFieldMappings.put( "email", "fb_email" );
        List<String> permissions = new ArrayList<String>();
        permissions.add( "email" );
        Backendless.UserService.loginWithFacebook( LoginActivity.this, null, facebookFieldMappings, permissions, loginCallback, true );

    }

    @OnClick(R.id.twitterLogin)
    public void twitterLogin(View view) {
        Map<String, String> twitterFieldsMappings = new HashMap<String, String>();
        twitterFieldsMappings.put( "name", "twitter_name" );
        Backendless.UserService.loginWithTwitter(LoginActivity.this, twitterFieldsMappings, loginCallback, true);
    }

    @OnClick(R.id.googlePlusLogin)
    public void googlePlusLogin(View view) {
        Map<String, String> googlePlusFieldsMapping = new HashMap<>();
        googlePlusFieldsMapping.put("email", "email");
        List<String> permissions = new ArrayList<>();
        Backendless.UserService.loginWithGooglePlus(LoginActivity.this, null, googlePlusFieldsMapping, permissions, loginCallback, true);
    }

    @OnClick(R.id.privacyPolicy)
    public void privacyPolicy(View view) {
        Intent intent = new Intent(this, PrivacyPolicy.class);
        startActivity(intent);
    }

}
