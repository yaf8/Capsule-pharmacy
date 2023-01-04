package com.example.capsule.fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.capsule.Product;
import com.example.capsulepharmacy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UploadFragment extends Fragment {

    private static final int CHOOSE_IMAGE = 101;
    private MaterialButton btnUpload;
    private EditText edtProductName, edtShortDescription, edtLongDescription, edtPrice;
    private ImageView productImage;
    private Product product;
    private Uri productImageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        initView(view);
        


        btnUpload.setOnClickListener(v -> uploadProduct());
        productImage.setOnClickListener(v -> setImage());



        // Inflate the layout for this fragment
        return view;
    }

    private void setImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Product Image" ), CHOOSE_IMAGE);
    }

    private void uploadProduct() {

        String productName = edtProductName.getText().toString();
        String shortDescription = edtShortDescription.getText().toString();
        String longDescription = edtLongDescription.getText().toString();
        String price = edtPrice.getText().toString();

        boolean isDataValid = validateData(productName, shortDescription, longDescription, price, productImageUri);

        if (!isDataValid){
            return;
        }

        Product product = new Product();
        product.setProductName(productName);
        product.setProductShortDescription(shortDescription);
        product.setProductLongDescription(longDescription);
        product.setProductPrice(price);
        product.setProductImageUri(productImageUri);
        uploadToDatabase();

    }

    private void uploadToDatabase() {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        CollectionReference prodRef = database.collection("products");

        Product product = new Product();
        product.setProductName(edtProductName.getText().toString());
        product.setProductShortDescription(edtShortDescription.getText().toString());
        product.setProductPrice(edtPrice.getText().toString());
        product.setProductLongDescription(edtLongDescription.getText().toString());

        //write
        Map<String, Object> data1 = new HashMap<>();
        data1.put("productName", product.getProductName());
        data1.put("shortDescription", product.getProductShortDescription());
        data1.put("longDescription", product.getProductLongDescription());
        data1.put("productPrice", product.getProductPrice());
        data1.put("productImageUri", "");
        //System.out.println("Product id : "+product.getProductID());
        prodRef.document().set(data1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getActivity(), "Data uploaded", Toast.LENGTH_SHORT).show();
                clearInput();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        database.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.get("productName"));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });





    }

    private void clearInput() {
        edtProductName.setText("");
        edtShortDescription.setText("");
        edtLongDescription.setText("");
        edtPrice.setText("");
        productImage.setImageResource(R.mipmap.product_upload_icon);
        productImage.setBackgroundColor(Color.WHITE);
    }

    private boolean validateData(String productName, String shortDescription, String longDescription, String price, Uri uri){

        if (productName.isEmpty()){
            edtProductName.setError("Product Name is invalid");
            return false;
        }

        if(shortDescription.isEmpty()){
            edtShortDescription.setError("Short Description is invalid");
            return false;
        }

        if (price.isEmpty()){
            edtPrice.setError("Price is invalid");
            return false;
        }

        if(longDescription.isEmpty()) {
            edtLongDescription.setError("Long Description is invalid");
            return false;
        }

        if (uri == null) {
            productImage.setBackgroundColor(Color.RED);
            return false;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && data != null && data.getData() != null){
            productImageUri = data.getData();

            productImage.setBackgroundColor(Color.WHITE);
            productImage.setImageURI(productImageUri);
        }

    }

    private void initView(View view) {
        btnUpload = view.findViewById(R.id.btnUpload);
        edtProductName = view.findViewById(R.id.edtProductName);
        edtShortDescription = view.findViewById(R.id.edtShortDescription);
        edtLongDescription = view.findViewById(R.id.edtLongDescription);
        edtPrice = view.findViewById(R.id.edtPrice);
        productImage = view.findViewById(R.id.productImage);
    }
}