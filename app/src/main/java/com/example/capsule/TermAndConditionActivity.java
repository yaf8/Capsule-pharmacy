package com.example.capsule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.capsulepharmacy.R;

public class TermAndConditionActivity extends AppCompatActivity {

    private TextView txtTerm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_condition);

        txtTerm = findViewById(R.id.txtTerm);

        String Term_and_Condition;
        Term_and_Condition = "1. You can be at any age to use this mobile app.\n" +
                "\n" +
                "2. You must agree to our Privacy Policy and Terms & Conditions before using the app.\n" +
                "\n" +
                "3. You must provide accurate information when registering for the app.\n" +
                "\n" +
                "4. All use of this mobile app is subject to the laws and regulations of your jurisdiction.\n" +
                "\n" +
                "5. You are solely responsible for maintaining the security of your account and all related activities under it, including but not limited to passwords, payment information, etc…\n" +
                "\n" +
                "6. You may not use this mobile app for any unlawful or illegal purposes, or in a manner that infringes upon the rights of others or violates any applicable laws or regulations. \n" +
                "\n" +
                "7. We reserve the right to monitor usage of this mobile app and take appropriate action if we believe any activity is being conducted in a manner that violates applicable laws or regulations or is detrimental to our interests or those of other users. \n" +
                "\n" +
                "8. We reserve the right to deny access or terminate accounts for any reason at our discretion without prior notice or liability. \n" +
                "\n" +
                "9. We will not be held liable for any damages resulting from your use of this mobile app, including but not limited to loss of data, interruption of service, unauthorized access, etc… \n" +
                "\n" +
                "10. We reserve the right to modify these Terms & Conditions at any time without prior notice to you";
        txtTerm.setText(Term_and_Condition);

    }
}