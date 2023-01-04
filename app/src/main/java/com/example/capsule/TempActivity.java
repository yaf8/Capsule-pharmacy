package com.example.capsule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.capsulepharmacy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class TempActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    Button btnRefresh;
    TextView FirstName, LastName, Email, Phone;
    public static String cUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        FirstName = findViewById(R.id.FirstName);
        LastName = findViewById(R.id.LastName);
        Email = findViewById(R.id.Email);
        Phone = findViewById(R.id.Phone);
        btnRefresh = findViewById(R.id.btnRefresh);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();

        btnRefresh.setOnClickListener(v -> {
            assert user != null;
            Email.setText(user.getDisplayName());
        });


    }
}