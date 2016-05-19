package com.alejandromoran.memegeneratorpro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.alejandromoran.memegeneratorpro.R;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Backendless.initApp(this, getString(R.string.backendles_app_id), getString(R.string.backendles_app_secret),  getString(R.string.backendles_app_version));
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void login(View view) {
        Backendless.UserService.loginWithFacebook( LoginActivity.this, new AsyncCallback<BackendlessUser>()
        {
            @Override
            public void handleResponse( BackendlessUser loggedInUser )
            {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void handleFault( BackendlessFault fault )
            {
                Log.d("DEBUG", fault.getMessage());
            }
        } );

    }

}
