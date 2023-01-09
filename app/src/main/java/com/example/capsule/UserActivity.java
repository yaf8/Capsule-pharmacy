package com.example.capsule;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.capsulepharmacy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity {

    private MaterialButton MatBtnDelete;
    private TextView txtFirstName, txtLastName, txtEmail, txtPhoneNumber, txtUserType;
    private CircleImageView profileImage;
    private FirebaseFirestore db, db2;
    private DocumentReference docRef, docRef2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        MatBtnDelete = findViewById(R.id.MatBtnDelete);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        profileImage = findViewById(R.id.profileImage);
        txtUserType = findViewById(R.id.txtType);



        loadData();

    }

    private void loadData() {

        Intent intent = getIntent();
        if(intent != null) {
            String userEmail = intent.getStringExtra(UserAdapter.USER_ID_KEY);
            if (userEmail != null){

                Users incomingUser = Utils.getInstance().getUserByEmail(userEmail);

                if (incomingUser != null) {
                    setData(incomingUser);
                    MatBtnDelete.setOnClickListener(v -> {
                        deleteUser(incomingUser);
                    });
                }

            }
        }


    }

    private void deleteUser(Users users) {
        db = FirebaseFirestore.getInstance();
        UserInfo userInfo = FirebaseAuth.getInstance().getCurrentUser();

        Map<String, Object> user = new HashMap<>();
        user.put("isDeleted", true);

        db.collection("Accounts/").document(users.getEmail())
                .update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UserActivity.this, "User Deleted Successfully", Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < Utils.getAllUsers().size(); i++){
                            if (Objects.equals(Utils.getAllUsers().get(i).getEmail(), users.getEmail())){
                                Utils.getAllUsers().remove(i);
                            }
                        }

                        startActivity(new Intent(UserActivity.this, ManageAccountActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void setData(Users users) {
        txtFirstName.setText(users.getFirstName());
        txtLastName.setText(users.getLastName());
        txtEmail.setText(users.getEmail());
        txtPhoneNumber.setText(users.getPhoneNumber());
        txtUserType.setText(users.getUserType());

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();


        assert user != null;
        if(user.getPhotoUrl() != null) {
            Glide.with(this)
                    .asBitmap()
                    .load(user.getPhotoUrl())
                    .into(profileImage);
        } else {
            Glide.with(this)
                    .asBitmap()
                    .load(users.getUrl())
                    .into(profileImage);
        }



    }
}