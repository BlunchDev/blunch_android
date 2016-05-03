package dev.blunch.blunch.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import dev.blunch.blunch.R;
import dev.blunch.blunch.services.MenuService;
import dev.blunch.blunch.services.ServiceFactory;
import dev.blunch.blunch.utils.Repository;

@SuppressWarnings("all")
public class LogInActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private ProfileTracker mProfileTracker;
    private FacebookCallback<LoginResult> loginResultFacebookCallback =
            new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.splash_screen);
                    relativeLayout.findViewById(R.id.login_button).setVisibility(View.GONE);
                    relativeLayout.findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
                    if(Profile.getCurrentProfile() == null) {
                        mProfileTracker = new ProfileTracker() {
                            @Override
                            protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                                mProfileTracker.stopTracking();
                                initApp();
                            }
                        };
                        mProfileTracker.startTracking();
                    }
                    else {
                        initApp();
                    }
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            };

    private MenuService menuService;
    private boolean logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_log_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        initializeServices();

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_photos"));
        loginButton.registerCallback(callbackManager, loginResultFacebookCallback);

        //Only for development purposes
        printKeyHash();

        ((ProgressBar) findViewById(R.id.progress_bar)).getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        if (Profile.getCurrentProfile() != null) {
            logIn = true;
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.splash_screen);
            relativeLayout.findViewById(R.id.login_button).setVisibility(View.GONE);
        } else {
            logIn = false;
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }

    }

    private void initializeServices() {
        menuService = ServiceFactory.getMenuService(getApplicationContext());
        menuService.setOnChangedListener(new Repository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                if (type.equals(EventType.Full) && logIn) {
                    initApp();
                }
            }
        });
        ServiceFactory.getCollaborativeMenuService(getApplicationContext());
        ServiceFactory.getPaymentMenuService(getApplicationContext());
    }

    private void initApp() {
        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void printKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("dev.blunch.blunch", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", e.toString());
        }
    }

}
