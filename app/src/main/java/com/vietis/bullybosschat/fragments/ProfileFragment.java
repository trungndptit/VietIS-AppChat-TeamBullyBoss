package com.vietis.bullybosschat.fragments;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.vietis.bullybosschat.R;
import com.vietis.bullybosschat.entrance.LoginActivity;
import com.vietis.bullybosschat.model.User;
import com.vietis.bullybosschat.utils.Constants;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private static final int IMAGE_CHOOSE  = 1;

    private ImageView mImageAvatar;
    private ImageView mImageCover;
    private ImageView mUpdateAvatar;
    private ImageView mUpdateCover;
    private TextView mTextName;
    private TextView tvFriends, tvFollows;
    private ImageView mImageLogout;
    private DatabaseReference mReference;

    private FirebaseUser user;
    private StorageReference mStorageReference;

    private StorageTask mUpLoadTask;
    private Uri mUrl;
    private boolean isUpdateAvatar = true;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_profile_fragment, container, false);
        mImageCover = view.findViewById(R.id.image_background);
        mUpdateAvatar = view.findViewById(R.id.image_edit_avatar);
        mUpdateCover = view.findViewById(R.id.image_edit_cover);
        mImageLogout =  view.findViewById(R.id.image_logout);
        mImageCover =  view.findViewById(R.id.image_background);
        mImageAvatar = view.findViewById(R.id.image_avatar);
        mTextName = view.findViewById(R.id.txt_name);
        tvFriends = view.findViewById(R.id.text_one);
        tvFollows = view.findViewById(R.id.text_two);

        user = FirebaseAuth.getInstance().getCurrentUser();

        final String idUser = user.getUid();
        mReference = FirebaseDatabase.getInstance().getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference("uploads");
        mReference.child("Users").child(idUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("Debug: addChild profile");
                User user = dataSnapshot.getValue(User.class);

                if (idUser.equals(user.getId())) {
                    mTextName.setText(user.getUsername());
                    tvFriends.setText(String.valueOf(user.getFriends().size()));
                    tvFollows.setText(String.valueOf(user.getFollows().size()-1));
                    if(user.getImagecover().equals("default")){
                        mImageCover.setImageResource(R.drawable.anhbia1);
                    }
                    else {
                        if (getActivity()!= null){
                            Glide.with(getActivity())
                                    .load(user.getImagecover())
                                    .into(mImageCover);
                        }
                    }
                    if (user.getImageurl().equals("default")) {
                        mImageAvatar.setImageResource(R.drawable.anh1);
                    } else {
                        if (getActivity()!= null){
                            Glide.with(getActivity())
                                    .load(user.getImageurl())
                                    .circleCrop()
                                    .into(mImageAvatar);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        addListner();
        return view;

    }

    private void addListner() {

        mImageLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity().getApplication(), LoginActivity.class);
                startActivity(intent);

            }
        });

        mUpdateCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
                isUpdateAvatar = false;
            }
        });


        mUpdateAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chooseImage();
                isUpdateAvatar = true;
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "choose photo for  profile"), IMAGE_CHOOSE);

    }

    private String getFileImage(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void upLoadImage() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Uploading");
        progressDialog.show();
        if (mUrl != null) {
            final StorageReference storageFile = mStorageReference.child(System.currentTimeMillis() + "." + getFileImage(mUrl));
            mUpLoadTask = storageFile.putFile(mUrl);
            mUpLoadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
//                        Toast.makeText(getContext(), "upload image fail", Toast.LENGTH_SHORT).show();

                    }
                    return storageFile.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Uri urlDowlaod = (Uri) task.getResult();
                        String mUriDowload = urlDowlaod.toString();
                        mReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
                        HashMap<String, Object> hashMap = new HashMap<>();
                        if (isUpdateAvatar) hashMap.put(Constants.ROW_AVATAR, mUriDowload);
                        else hashMap.put(Constants.ROW_COVER, mUriDowload);
                        mReference.updateChildren(hashMap);
                        progressDialog.dismiss();

                    } else {
                        Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                }
            });


        } else {
            Toast.makeText(getContext(), "Not Select Image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CHOOSE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mUrl = data.getData();

            if (mUpLoadTask != null && mUpLoadTask.isInProgress()) {
                Toast.makeText(getContext(), "image  is uploading", Toast.LENGTH_SHORT).show();
            } else {
                upLoadImage();

            }
        }

    }
}

