package com.example.capsule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capsule.fragments.ProfileFragment;
import com.example.capsulepharmacy.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnSignIn;
    TextView txtSignUp;
    ProgressBar progressBar;
    CheckBox checkAdmin;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        txtSignUp = findViewById(R.id.txtSignUp);
        progressBar = findViewById(R.id.progressBar);
        checkAdmin = findViewById(R.id.CheckBoxAdmin);


        progressBar.setVisibility(View.INVISIBLE);

        btnSignIn.setOnClickListener(v -> login());

        txtSignUp.setOnClickListener(v -> startActivity(new Intent(this, CreateAccountActivity.class)));

    }

    private void login() {

        String email, password;
        email  = edtEmail.getText().toString();
        password = edtPassword.getText().toString();

        boolean isValidated = validateData(email, password);
        if (!isValidated){
            return;
        }

        loginAccountInFirebase(email, password);

    }

    private void loginAccountInFirebase(String email, String password) {
        changeInProgress(true);

        firebaseAuth = FirebaseAuth.getInstance();


        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);

                if(task.isSuccessful()){
                    //login successful
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        if (checkAdmin.isChecked()){
                            boolean isAdmin = AdminCheck();
                            if (isAdmin) {
                                Toast.makeText(LoginActivity.this, "Logged in as ADMIN", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "Logged is as USER", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // go to main activity
                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Email not verify, Please verify your email.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    //login failure
                    Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    private boolean AdminCheck() {

        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = firebaseFirestore.document("Accounts");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



        return true;
    }

    private void changeInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            btnSignIn.setVisibility(View.VISIBLE);
        }
    }

    private boolean validateData(String email, String password){

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            edtEmail.setError("Email is invalid");
            return false;
        }

        if (password.length() < 8){
            edtPassword.setError("Password length is invalid");
            return false;
        }

        return true;
    }
}