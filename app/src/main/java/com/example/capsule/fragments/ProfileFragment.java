package com.example.capsule.fragments;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.capsule.HelpAndSupportActivity;
import com.example.capsule.LoginActivity;
import com.example.capsule.ManageAccountActivity;
import com.example.capsule.PrivacyPolicyActivity;
import com.example.capsule.TermAndConditionActivity;
import com.example.capsulepharmacy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class ProfileFragment extends Fragment {

    private static final int CHOOSE_IMAGE = 101;
    TextView txtEmail, txtBtnPrivacy, txtBtnHelp, txtBtnTerm,  txtBtnLogout;
    public static TextView txtBtnManage;
    private ShapeableImageView imageProfile;
    private TextView txtFullName;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;
    Uri uriProfileImage;
    private StorageReference storageReference;
    private String downloadProfileImageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //txtEmail.setText("firebaseUser.getEmail()");
        //db = FirebaseFirestore.getInstance();

        initView(view);


        loadEmailFromFirestore();


        firebaseAuth = FirebaseAuth.getInstance();


        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            txtFullName.setText(firebaseUser.getDisplayName());
        }

        imageProfile.setOnClickListener(v -> {
            selectImage();
        });

        loadUserInfo();

        // UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
        //        .setDisplayName(edtFirstName.getText().toString())
        //         .build();
        //assert user != null;
        //user.updateProfile(profile);


        txtBtnLogout.setOnClickListener(v -> {

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut(); //signout from fire base if login with custom

            Toast.makeText(getActivity(), "Sign out", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });

        txtBtnPrivacy.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), PrivacyPolicyActivity.class));
        });

        txtBtnHelp.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), HelpAndSupportActivity.class));
        });

        txtBtnTerm.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), TermAndConditionActivity.class));
        });
        txtBtnManage.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ManageAccountActivity.class));
        });


        // Inflate the layout for this fragment
        return view;
    }


    @SuppressLint("SetTextI18n")
    private void readUserData() {

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //CollectionReference colRef = db.collection("Accounts");

        //-----------------------------------Read_Data-----------------------------
        //System.out.println("user.getEmail() : " + user.getEmail());
        DocumentReference docRef = db.collection("Accounts/").document(Objects.requireNonNull(firebaseUser.getEmail()));
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {

            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    if (Boolean.TRUE.equals(snapshot.getBoolean("isAdmin"))) {
                        txtBtnManage.setVisibility(View.VISIBLE);
                    } else {
                        txtBtnManage.setVisibility(View.GONE);
                    }
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });


        //-----------------------------------Read_Data-----------------------------
    }




    private void initView(View view) {

        txtFullName = view.findViewById(R.id.txtFullName);
        txtEmail = view.findViewById(R.id.txtEmailAddress);
        txtBtnPrivacy = view.findViewById(R.id.txtBtnPrivacy);
        txtBtnHelp = view.findViewById(R.id.txtBtnHelp);
        txtBtnTerm = view.findViewById(R.id.txtBtnTerm);
        txtBtnLogout = view.findViewById(R.id.txtBtnLogout);
        imageProfile = view.findViewById(R.id.imageProfile);
        txtBtnManage = view.findViewById(R.id.txtBtnManage);
    }

    private void loadEmailFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
    }

    private void loadUserInfo() {
        if (firebaseUser != null) {

            UserInfo info = FirebaseAuth.getInstance().getCurrentUser();
            txtEmail.setText(info.getEmail());

            if (firebaseUser.getPhotoUrl() != null)
                Glide.with(requireActivity())
                        .load(firebaseUser.getPhotoUrl())
                        .into(imageProfile);
        }
    }

    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && data != null) {
            uriProfileImage = data.getData();
            try {
                 // Setting image on image view using Bitmap
                 // Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uriProfileImage);
                 // imageProfile.setImageBitmap(bitmap);
                imageProfile.setImageURI(uriProfileImage);

                uploadImage();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = simpleDateFormat.format(now);


        storageReference = FirebaseStorage.getInstance().getReference("profileImage/" + fileName);

        storageReference.putFile(uriProfileImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadProfileImageUrl = uri.toString();
                                System.out.println("URI PATH : " + uri.toString());


                                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(uri)
                                        .build();
                                System.out.println("Image URI before upload : " + uriProfileImage);
                                System.out.println("Download URL value : " + downloadProfileImageUrl);
                                firebaseUser.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {

                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                            Toast.makeText(getActivity(), "Profile Image Updated", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });


                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        imageProfile.setImageResource(R.mipmap.profile_icon);
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
