package com.example.capsule;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.capsule.fragments.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class Utils {
    private static Utils instance;
    private static ArrayList<Product> allProducts;
    public static int idNum;
    private static ArrayList<Users> allUsers;
    private static ArrayList<String> allUsersEmail;
    private FirebaseFirestore db;

    private Utils() {
        if (allProducts == null)
            allProducts = new ArrayList<>();

        if (allUsers == null)
            allUsers = new ArrayList<>();

        allUsersEmail = new ArrayList<>();

        initData();
        initUserData();
    }

    private void initUserData() {

        db = FirebaseFirestore.getInstance();

        //CollectionReference colRef = db.collection("Accounts");

        //-----------------------------------Read_Data-----------------------------
        UserInfo userInfo = FirebaseAuth.getInstance().getCurrentUser();

        //DocumentReference docRef = db.collection("Accounts/").document(Objects.requireNonNull(userInfo.getEmail()));


        //for (String email : allUsersEmail) {
        //  System.out.println("allUsersEmail///////////// : " + email);

        final boolean[] checkAdd = {true};
        final String[] checkEmail = {""};

        db.collection("Accounts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {


                        assert value != null;

                        for (QueryDocumentSnapshot doc : value) {
                            //System.out.println();

                            //for (int i = 0; i < Utils.getAllUsers().size(); i++) {
                            //    if (Objects.equals(Utils.getAllUsers().get(i).getEmail(), doc.getString("email"))) {
                            //        checkAdd[0] = false;
                            //    } else {
                            //        checkAdd[0] = true;
                            //    }

                            //     if (checkAdd[0]) {

                            System.out.println("firstName : " + doc.getString("firstName"));
                            System.out.println("lastName : " + doc.getString("lastName"));
                            System.out.println("PhoneNumber: " + doc.getString("PhoneNumber"));
                            System.out.println("profileURL : " + doc.getString("profileURL"));
                            System.out.println("Email : " + doc.getString("email"));
                            System.out.println("isAdmin : " + doc.getBoolean("isAdmin"));
                            System.out.println("isDeleted : " + doc.getBoolean("isDeleted"));
                            System.out.println("userType : " + doc.getString("userType"));


                            allUsers.add(new Users(
                                    Objects.requireNonNull(doc.getString("firstName")),
                                    Objects.requireNonNull(doc.getString("lastName")),
                                    Objects.requireNonNull(doc.getString("PhoneNumber")),
                                    Objects.requireNonNull(doc.getString("profileURL")),
                                    Objects.requireNonNull(doc.getString("email")),
                                    Boolean.TRUE.equals(doc.getBoolean("isAdmin")),
                                    Boolean.TRUE.equals(doc.getBoolean("isDeleted")),
                                    Objects.requireNonNull(doc.getString("userType"))
                            ));
                            //ManageAccountActivity.adapter.notifyDataSetChanged();
                            //}
                            //}
                        }

                        Log.d(TAG, "Current cites in CA: " + allUsersEmail);
                    }
                });
        //}
        //-----------------------------------Read_Data-----------------------------


        //allUsers.add(new User("MED"+idNum, "Panadol", "PANADOL 500mg FILM-COATED TABLETS", LongDescription, 50.13, url));


    }

    private void listToMultipleDocument() {

        db = FirebaseFirestore.getInstance();


        db.collection("Accounts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("email") != null) {
                                allUsersEmail.add(doc.getString("email"));
                                System.out.println("doc.getString(email)" + doc.getString("email"));
                            }
                        }
                        Log.d(TAG, "Current cites in CA: " + allUsersEmail);
                        for (String email : allUsersEmail) {
                            System.out.println("Email 1 2 3 : " + allUsersEmail);
                        }
                    }
                });
    }


    private void initData() {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.get("productName"));
                                allProducts.add(new Product(
                                        document.getId(),
                                        document.get("productName").toString(),
                                        document.get("shortDescription").toString(),
                                        document.get("longDescription").toString(),
                                        document.get("productPrice").toString(),
                                        document.get("productImageUri").toString())
                                );
                                HomeFragment.adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public static Utils getInstance() {
        if (null != instance)
            return instance;
        else {
            instance = new Utils();
            return instance;
        }
    }

    public static ArrayList<Users> getAllUsers() {

        return allUsers;

    }

    public static ArrayList<Product> getAllProducts() {
        return allProducts;
    }

    public Product getProductById(String id) {
        for (Product p : allProducts) {
            if (Objects.equals(p.getProductID(), id)) {
                return p;
            }
        }

        return null;
    }

    public Users getUserByEmail(String email) {
        for (Users u : allUsers) {
            if (Objects.equals(u.getEmail(), email)) {
                return u;
            }
        }

        return null;
    }
}
