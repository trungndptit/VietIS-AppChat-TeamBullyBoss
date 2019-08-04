package com.vietis.bullybosschat.entrance;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.vietis.bullybosschat.R;
import com.vietis.bullybosschat.cache.PrefUtils;
import com.vietis.bullybosschat.fragments.HomeChatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText mInputEmail;
    private EditText mInputPasword;
    private Button mButtonLogin;
    private ImageButton mImageBack;

    private FirebaseAuth mAuth;

    private PrefUtils prefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefUtils = PrefUtils.getIntance(this);
        mAuth = FirebaseAuth.getInstance();
        mInputEmail = findViewById(R.id.et_email);
        mInputPasword = findViewById(R.id.et_password);
        mImageBack =  findViewById(R.id.image_back);
        mButtonLogin = findViewById(R.id.button_login);
        addListner();

    }

    private void addListner() {
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mInputEmail.getText().toString();
                String password = mInputPasword.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "All  filed  are  requeied", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(LoginActivity.this, "password must be at  least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                prefUtils.setCurrentUid(mAuth.getUid());
                                Intent intent = new Intent(LoginActivity.this, HomeChatActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(LoginActivity.this, "Login  fail", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });


        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
