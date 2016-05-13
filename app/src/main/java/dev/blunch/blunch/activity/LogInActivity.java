package dev.blunch.blunch.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.User;
import dev.blunch.blunch.services.MenuService;
import dev.blunch.blunch.services.ServiceFactory;
import dev.blunch.blunch.utils.Preferences;
import dev.blunch.blunch.utils.Repository;

@SuppressWarnings("all")
public class LogInActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private ProfileTracker mProfileTracker;

    private String email;
    private String name;

    private boolean prof;
    private boolean graph;

    private FacebookCallback<LoginResult> loginResultFacebookCallback;

    private void createUser() {
        if (menuService.findUserByEmail(email) == null) {
            String imageFile = getImageStringFile();
            menuService.createNewUser(new User(
                    name,
                    email,
                    imageFile
            ));
        }

        initApp();
    }

    private String getImageStringFile() {
        try {
            URL image = new URL(Profile.getCurrentProfile()
                    .getProfilePictureUri(140, 140).toString());

            Resources res = getResources();
            Bitmap src = BitmapFactory.decodeStream(image.openConnection().getInputStream());

            ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
            src.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
            src.recycle();
            byte[] byteArray = bYtE.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private MenuService menuService;
    private boolean logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Preferences.init(getApplicationContext());

        loginResultFacebookCallback =
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());
                                        try {
                                            String s = object.getString("email");
                                            s = s.replaceAll("\\.", "@");
                                            email = s;
                                            Log.d("email", s);
                                            Preferences.setCurrentUserEmail(email);
                                            name = object.getString("name");
                                            graph = true;
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        if (prof && graph) {
                                            createUser();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email");
                        request.setParameters(parameters);
                        request.executeAsync();

                        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.splash_screen);
                        relativeLayout.findViewById(R.id.login_button).setVisibility(View.GONE);
                        relativeLayout.findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
                        if(Profile.getCurrentProfile() == null) {
                            mProfileTracker = new ProfileTracker() {
                                @Override
                                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                                    mProfileTracker.stopTracking();
                                    prof = true;
                                    if (prof && graph) {
                                        createUser();
                                    }
                                }
                            };
                            mProfileTracker.startTracking();
                        }
                        else {
                            prof = true;
                            if (prof && graph) {
                                createUser();
                            }
                        }
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                };

        prof = false;
        graph = false;
        logIn = false;

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_log_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        initializeServices();

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_photos", "email"));
        loginButton.registerCallback(callbackManager, loginResultFacebookCallback);

        //Only for development purposes
        printKeyHash();

        ((ProgressBar) findViewById(R.id.progress_bar)).getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        if (Profile.getCurrentProfile() != null) {
            logIn = true;
            email = Preferences.getCurrentUserEmail().replaceAll("\\.", "@");
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

    @Override
    public void onBackPressed()
    {
        finishAffinity();
    }

}
