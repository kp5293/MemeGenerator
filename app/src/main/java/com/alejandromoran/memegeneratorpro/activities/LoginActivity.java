package com.alejandromoran.memegeneratorpro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.alejandromoran.memegeneratorpro.R;
import com.alejandromoran.memegeneratorpro.entities.Meme;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Backendless.initApp(
                this,
                getString(R.string.backendles_app_id),
                getString(R.string.backendles_app_secret),
                getString(R.string.backendles_app_version)
        );
        Backendless.Persistence.mapTableToClass("Memes", Meme.class);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

       // AccessToken.refreshCurrentAccessTokenAsync();




        BackendlessUser user = Backendless.UserService.CurrentUser();

        if(user !=null) {
            Log.e("BackendlessLogin", String.valueOf("TRUE"));
        }
        else {
            Log.e("BackendlessLogin", String.valueOf("FALSE"));
        }

        login();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void login() {
        final List<String> permissions = new ArrayList<>();
        Map<String, String> facebookFieldMappings = new HashMap<String, String>();

        permissions.add("public_profile");
        permissions.add("user_friends");
        permissions.add("email");
        facebookFieldMappings.put("email", "fb_email");

        Backendless.UserService.loginWithFacebookSdk(this, facebookFieldMappings, permissions, callbackManager, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser loggedInUser) {
                //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                //startActivity(intent);
                Toast.makeText(LoginActivity.this, loggedInUser.getEmail(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(LoginActivity.this, "inccorect login", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void login(View view) {

        login();

    }

}
