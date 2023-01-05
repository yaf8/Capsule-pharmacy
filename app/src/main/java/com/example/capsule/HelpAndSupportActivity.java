package com.example.capsule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.capsulepharmacy.R;

public class HelpAndSupportActivity extends AppCompatActivity {

    private TextView textHelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_and_support);
        textHelp = findViewById(R.id.txtHelp);

        String help_and_support;

        help_and_support = "1. Provide access to user manuals and documentation\n" +
                "2. Provide support for troubleshooting technical issues\n" +
                "3. Offer online chat or email support\n" +
                "4. Provide tutorials or video demonstrations\n" +
                "5. Offer an FAQ page with frequently asked questions\n" +
                "6. Create a forum or discussion board for users to share tips, tricks, and experiences \n" +
                "7. Offer an online knowledge base with searchable answers \n" +
                "8. Provide access to a customer support team via telephone or live chat \n" +
                "9. Allow users to submit feedback or feature requests \n" +
                "10. Monitor social media channels for user inquiries";

        textHelp.setText(help_and_support);

    }
}