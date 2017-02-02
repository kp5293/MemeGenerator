package com.alejandromoran.memegeneratorpro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alejandromoran.memegeneratorpro.BuildConfig;
import com.alejandromoran.memegeneratorpro.R;
import com.facebook.FacebookSdk;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            //startActivity(SignedInActivity.createIntent(this, null));
            //finish();
            Toast.makeText(this, "already logged in", Toast.LENGTH_LONG).show();
        }
        else {
            startActivityForResult (
                    AuthUI. getInstance ()
                            .createSignInIntentBuilder()
                            .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build()))
                            .setIsSmartLockEnabled ( ! BuildConfig. DEBUG )
                            .build(),
                    RC_SIGN_IN);
        }

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                //startActivity(SignedInActivity.createIntent(this, response));
                Toast.makeText(this, "success logged in", Toast.LENGTH_LONG).show();
                //finish();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar(R.string.unknown_error);
                    return;
                }
            }

            showSnackbar(R.string.unknown_sign_in_response);
        }
    }

    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        //Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
        Toast.makeText(this, errorMessageRes, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @OnClick(R.id.facebookLogin)
    public void facebookLogin(View view) {

    }

    @OnClick(R.id.twitterLogin)
    public void twitterLogin(View view) {
    }

    @OnClick(R.id.googlePlusLogin)
    public void googlePlusLogin(View view) {
    }

    @OnClick(R.id.privacyPolicy)
    public void privacyPolicy(View view) {
        Intent intent = new Intent(this, PrivacyPolicy.class);
        startActivity(intent);
    }

}
