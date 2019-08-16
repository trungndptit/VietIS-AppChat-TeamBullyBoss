package com.vietis.bullybosschat.entrance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vietis.bullybosschat.R;
import com.vietis.bullybosschat.cache.PrefUtils;
import com.vietis.bullybosschat.fragments.HomeChatActivity;
import com.vietis.bullybosschat.utils.Constants;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TextView mTextRegitser;
    private Button mButtonEmail;

    private String TAG = " MainActivity";
    private LoginButton mLoginFb;
    private Button mButtonFb;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private SignInButton mLoginGg;
    private Button mButtonGg;

    private FirebaseAuth mAuth;
    private CallbackManager mCallBackManager;

    private PrefUtils prefUtils;

    private GoogleSignInClient mGoogleSignInClient;

    private static final int RC_GOOGLE_SIGN_IN = 001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefUtils = PrefUtils.getIntance(this);
        checkLogined();
        initView();

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();

                if ((user != null)) {
                    Log.d("main", "aaa" + user.getUid());
                } else {
                    Log.d("ad", "logout");
                }
            }
        };

        initLoginWithFb();
        initLoginWithGg();
        addlistner();
    }

    private void checkLogined() {
        if (prefUtils.getCurrentUid() != null) {
            Intent intent = new Intent(MainActivity.this, HomeChatActivity.class);
            startActivity(intent);
            finish();
        }
    }


    private void initView() {
        mAuth = FirebaseAuth.getInstance();
        mTextRegitser = findViewById(R.id.text_sigup);
        mButtonEmail = findViewById(R.id.button_login_email);
        mLoginFb = findViewById(R.id.loginfb_button);
        mLoginGg = findViewById(R.id.loginGg_button);
        mButtonFb = findViewById(R.id.btn_fb);
        mButtonGg = findViewById(R.id.btn_gg);

    }

    @Override
    public void onBackPressed() {
    }

    private void initLoginWithGg() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void initLoginWithFb() {

        mCallBackManager = CallbackManager.Factory.create();
        mLoginFb.setReadPermissions("email", "public_profile");
        mLoginFb.registerCallback(mCallBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);

            }
        });

    }

    public void onClickFacebookButton(View view) {
        if (view == mButtonFb) {
            mLoginFb.performClick();
        }
    }

    public void onClickGoogleButton(View view) {
        if (view == mButtonGg) {
            mLoginGg.performClick();
            signIn();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
        mCallBackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);


    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            createNewUser();

                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Log.d(TAG, "signInWithCredential" + task.isSuccessful());

                if (task.isSuccessful()) {

                    createNewUser();

                }
            }
        });
    }

    private void createNewUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        String idUser = user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(idUser);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", idUser);
        hashMap.put("username", user.getDisplayName());
        hashMap.put(Constants.ROW_AVATAR, "default");
        hashMap.put("state", "off");
        hashMap.put("search", "");
        hashMap.put(Constants.ROW_COVER, "default");


        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    prefUtils.setCurrentUid(mAuth.getUid());
                    Intent intent = new Intent(MainActivity.this, HomeChatActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Đã có lỗi đéo j xảy ra, vui lòng đửng thử lần 2 nhé , ok?", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void addlistner() {
        mTextRegitser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mButtonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}


