package com.example.capsule;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.capsule.fragments.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class Utils {
    private static Utils instance;
    private static ArrayList<Product> allProducts;
    public static int idNum;

    private Utils() {
        idNum++;
        if (allProducts == null)
            allProducts = new ArrayList<>();


        initData();
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


        //allProducts.add(new Product("MED"+idNum, "Panadol", "PANADOL 500mg FILM-COATED TABLETS", LongDescription, 50.13, url));


    }

    public static Utils getInstance() {
        if (null != instance)
            return instance;
        else{
            instance = new Utils();
            return instance;
        }
    }

    public static ArrayList<Product> getAllProducts() {
        return allProducts;
    }

    public Product getProductById(String id){
        for (Product p : allProducts)
        {
            if (Objects.equals(p.getProductID(), id)){
                return p;
            }
        }

        return null;
    }




    ///////////////////////////////////////////////////////////////////////
    String LongDescription = "Panadol contains paracetamol; recognised by the medical profession as effective medication " +
            "for you and your family. Panadol is indicated for: Headache, Colds & Influenza, Backache, Period Pain, Pain of Osteoarthritis, " +
            "Muscle Pain, Toothache, Rheumatic Pain.";
    String url = "https://oneononepharmacy.com/wp-content/uploads/Panadol-Tab-24.jpg";
    //////////////////////////////////////////////////////////////////////
}
