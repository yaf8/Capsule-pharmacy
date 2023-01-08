package com.example.capsule;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.RelativeLayout;

import com.example.capsulepharmacy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ManageAccountActivity extends AppCompatActivity {

    public static UserAdapter adapter;
    private RecyclerView recyclerView;
    private RelativeLayout relativeParent;
    //private ArrayList<Users> userList;
    private Users users;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);
        relativeParent = findViewById(R.id.relativeParent);
        recyclerView = findViewById(R.id.recycleViewer);

        //userList = new ArrayList<>();

        adapter = new UserAdapter(this);

        adapter.setUser(Utils.getInstance().getAllUsers());

        //System.out.println("AllUsersSize : " + Utils.getAllUsers().size());

        relativeParent.setOnClickListener(v -> adapter.notifyDataSetChanged());

        recyclerView.setAdapter(adapter);

        new Handler().postDelayed(() -> adapter.notifyDataSetChanged(), 3000);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}