package com.example.capsule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.capsulepharmacy.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private TextView txtPrivacy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        txtPrivacy = findViewById(R.id.txtPrivacy);

        String privacy_and_security;

        privacy_and_security = "Privacy:\n" +
                "\n" +
                "1. All customer data is securely stored and encrypted in Firebase databases.\n" +
                "2. The app requires password authentication to gain access to the user’s account.\n" +
                "3. All data transmitted through the app is encrypted using SSL/TLS protocols.\n" +
                "4. Customers can opt-out of sharing their personal information with third parties, such as delivery companies or payment processors.\n" +
                "5. The app will not share customer data with any other third parties without explicit permission from the customer.\n" +
                "\n" +
                "Security: \n" +
                "1. The app utilizes Firebase’s user authentication system for authentication and verification of users. \n" +
                "2. Access to the system is restricted using strong passwords and two-factor authentication when available. \n" +
                "3. Data is stored securely in Firebase databases with encryption enabled for added protection against unauthorized access or data breaches. \n" +
                "4. The app utilizes HTTPS protocol for secure transmission of all data sent between users and the server, as well as between users and delivery companies or payment processors when necessary for a transaction or order completion process \n" +
                "5. Regular security scans are conducted to detect potential vulnerabilities, and any security issues are patched promptly upon identification to minimize potential risks associated with malicious actors attempting to exploit those vulnerabilities";
        txtPrivacy.setText(privacy_and_security);
    }
}