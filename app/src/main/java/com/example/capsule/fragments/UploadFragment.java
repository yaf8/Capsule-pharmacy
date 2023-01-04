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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UploadFragment extends Fragment {

    private static final int CHOOSE_IMAGE = 101;
    private MaterialButton btnUpload;
    private EditText edtProductName, edtShortDescription, edtLongDescription, edtPrice;
    private ImageView productImage;
    private Product product;
    private StorageReference storageReference;
    private Uri productImageUri;
    private String downloadProfileImageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        initView(view);
        


        btnUpload.setOnClickListener(v -> uploadProduct());
        productImage.setOnClickListener(v -> selectImage());



        // Inflate the layout for this fragment
        return view;
    }

    private void selectImage() {
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

        product = new Product();
        product.setProductName(productName);
        product.setProductShortDescription(shortDescription);
        product.setProductLongDescription(longDescription);
        product.setProductPrice(price);
        product.setImageUrl(downloadProfileImageUrl);
        uploadToDatabase();

    }

    private void uploadToDatabase() {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        CollectionReference prodRef = database.collection("products");

        //write
        Map<String, Object> data1 = new HashMap<>();
        data1.put("productName", product.getProductName());
        data1.put("shortDescription", product.getProductShortDescription());
        data1.put("longDescription", product.getProductLongDescription());
        data1.put("productPrice", product.getProductPrice());
        data1.put("productImageUri", product.getImageUrl());

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

        if (requestCode == CHOOSE_IMAGE && data != null){
            productImageUri = data.getData();

            productImage.setBackgroundColor(Color.WHITE);
            productImage.setImageURI(productImageUri);
            uploadImage();
        }

    }

    private void uploadImage() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = simpleDateFormat.format(now);


        storageReference = FirebaseStorage.getInstance().getReference("productImage/" + fileName);

        storageReference.putFile(productImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadProfileImageUrl = uri.toString();
                                System.out.println("URI PATH : " + uri);


                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        productImage.setImageResource(R.mipmap.product_upload_icon);
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
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