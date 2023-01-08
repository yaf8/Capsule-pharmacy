package com.example.capsule;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capsulepharmacy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class CreateAccountActivity extends AppCompatActivity {

    EditText edtEmail, edtPharmacyName, edtPhoneNumber, edtPassword, edtConfirmPassword;
    Button btnCreateAccount;
    TextView txtSignIn;
    ProgressBar progressBar;
    FirebaseFirestore db, db2;
    public static String em;

    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        firebaseFirestore = FirebaseFirestore.getInstance();

        edtPharmacyName = findViewById(R.id.edtPharmacyName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        txtSignIn = findViewById(R.id.txtSignIn);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        btnCreateAccount.setOnClickListener(v -> createAccount());

        txtSignIn.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

    }

    private void createAccount() {
        String pharmacName, phoneNumber, email, password, confirmPassword;
        pharmacName = edtPharmacyName.getText().toString();
        phoneNumber = edtPhoneNumber.getText().toString();
        email  = edtEmail.getText().toString();
        password = edtPassword.getText().toString();
        confirmPassword = edtConfirmPassword.getText().toString();



        boolean isValidated = validateData(pharmacName, phoneNumber, email, password, confirmPassword);
        if (!isValidated){
            return;
        }

        createAccountInFirebase(email, password);
        saveToFirebaseFirestore(pharmacName, phoneNumber, email);

    }

    private void saveUserData(){


        db = FirebaseFirestore.getInstance();
        db2 = FirebaseFirestore.getInstance();
        UserInfo userInfo = FirebaseAuth.getInstance().getCurrentUser();

        //------------------------------------Save_Data----------------------------

        Map<String, Object> user = new HashMap<>();
        user.put("email", edtEmail.getText().toString());
        user.put("isAdmin", false);
        user.put("isDeleted", false);
        user.put("password", edtPassword.getText().toString());
        user.put("PhoneNumber", edtPhoneNumber.getText().toString());
        user.put("firstName", edtPharmacyName.getText().toString());
        user.put("lastName", "pharmacy");
        user.put("userType", "Pharmacy");
        user.put("profileURL", "https://icon-library.com/images/user-icon-jpg/user-icon-jpg-8.jpg");

        Map<String, Object> userEmail = new HashMap<>();
        userEmail.put("userEmail",userInfo.getEmail());

        db.collection("Accounts/").document(edtEmail.getText().toString())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CreateAccountActivity.this, "DocumentSnapshot successfully written!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "Error writing document", e);
                    }
                });

        //------------------------------------Save_Data----------------------------
    }

    private void saveToFirebaseFirestore(String pharmacyName, String phoneNumber, String email) {
        Map<String, Object> user = new HashMap<>();

        user.put("pharmacyName", pharmacyName);
        user.put("phoneNumber", phoneNumber);
        user.put("email", email);
        em = email;

        firebaseFirestore.collection("pharmacy").document(email).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CreateAccountActivity.this, "Successfully saved", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateAccountActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



    }

    private void createAccountInFirebase(String email, String password) {
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()){

                    saveUserData();
                    //uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu
                    //Todo: test profile update
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                            .setDisplayName(edtPharmacyName.getText().toString())
                            .build();
                    assert user != null;
                    user.updateProfile(profile);

                    //todo: test profile update
                    //uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu

                    //create account is done
                    //firebaseAuth.getCurrentUser().sendEmailVerification();

                    Toast.makeText(CreateAccountActivity.this, "Successfully created account", Toast.LENGTH_SHORT).show();

                    firebaseAuth.signOut();

                } else {

                    //failure
                    Toast.makeText(CreateAccountActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    private void changeInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            btnCreateAccount.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            btnCreateAccount.setVisibility(View.VISIBLE);
        }
    }

    private boolean validateData(String pharmacyName, String phoneNumber, String email, String password, String confirmPassword){

        if (pharmacyName == null){
            edtPharmacyName.setError("Name is empty");
            return false;
        }


        if(phoneNumber.length() < 10 && !Patterns.PHONE.matcher(phoneNumber).matches()){
            edtPhoneNumber.setError("Invalid phone");
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            edtEmail.setError("Email is invalid");
            return false;
        }

        if (password.length() < 8){
            edtPassword.setError("Password length is invalid");
            return false;
        }

        if (!password.equals(confirmPassword)){
            edtConfirmPassword.setError("Password not matched");
            return false;
        }

        return true;
    }

}