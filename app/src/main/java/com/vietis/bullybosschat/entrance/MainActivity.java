package com.vietis.bullybosschat.entrance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.vietis.bullybosschat.fragments.HomeChatActivity;
import com.vietis.bullybosschat.R;

public class MainActivity extends AppCompatActivity {

    private TextView mTextRegitser;
    private Button mButtonEmail;

    private String TAG = " MainActivity";
    private LoginButton mLoginFb;
    private Button mButtonFb;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private  LoginButton mLoginGg;

    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private CallbackManager mCallBackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
//        reference = FirebaseDatabase.getInstance().getReference();
//        System.out.println("Debug: ngoai");
////        reference.child("ga").push().setValue("debug 01");
//        reference.child("Users").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
////                String debug = dataSnapshot.getValue().toString();
//                System.out.println("Debug: addChild" );
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();

                if ((user != null)) {
                    Log.d("main", "aaa" + user.getUid());
                } else {
                    Log.d("ad", "logut");
                }
            }
        };

        initLoginWithFb();
        initLoginWithGg();
        addlistner();
    }


    private void initView() {
        mAuth = FirebaseAuth.getInstance();
        mTextRegitser = findViewById(R.id.text_sigup);
        mButtonEmail = findViewById(R.id.button_login_email);
        mLoginFb = findViewById(R.id.loginfb_button);
        mButtonFb = findViewById(R.id.btn_fb);
    }

    private void initLoginWithGg() {

    }

    private void initLoginWithFb() {
        mCallBackManager = CallbackManager.Factory.create();
        mLoginFb.setReadPermissions("email", "public_profile");
        mLoginFb.registerCallback(mCallBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                Intent intent =  new Intent(MainActivity.this, HomeChatActivity.class);
                startActivity(intent);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallBackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Log.d(TAG, "signInWithCredential" + task.isSuccessful());
                if (!task.isSuccessful()) {

                    Toast.makeText(MainActivity.this, "signInWithCredential Failed", Toast.LENGTH_SHORT).show();
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


