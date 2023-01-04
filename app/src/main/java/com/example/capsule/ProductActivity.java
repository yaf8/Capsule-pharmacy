package com.example.capsule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.capsule.fragments.HomeFragment;
import com.example.capsulepharmacy.R;

import java.util.ArrayList;
import java.util.Objects;

public class ProductActivity extends AppCompatActivity {



    ImageView imgProduct;
    TextView txtProductName, txtShortDescription, txtLongDescription, txtProductPrice;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        initView();

        Intent intent = getIntent();
        if (intent != null) {

            String productID = intent.getStringExtra(HomeFragment.PRODUCT_ID_KEY);

            if (productID != null){
                Product incomingProduct = Utils.getInstance().getProductById(productID);

                if (incomingProduct != null){
                    setData(incomingProduct);
                }
            }
        }


    }




    private void setData(Product product) {
        txtProductName.setText(product.getProductName());
        txtShortDescription.setText(product.getProductShortDescription());
        txtLongDescription.setText(product.getProductLongDescription());
        txtProductPrice.setText(String.valueOf(product.getProductPrice()));
        Glide.with(this)
                .asBitmap()
                .load(product.getProductImageUri())
                .into(imgProduct);

    }

    protected void initView(){
        imgProduct = findViewById(R.id.imgProduct);
        txtProductName = findViewById(R.id.txtProductName);
        txtShortDescription = findViewById(R.id.txtShortDescription);
        txtLongDescription = findViewById(R.id.txtLongDescription);
        txtProductPrice = findViewById(R.id.txtProductPrice);
        btnAdd = findViewById(R.id.btnAdd);
    }

    String LongDescription = "Panadol contains paracetamol; recognised by the medical profession as effective medication " +
            "for you and your family. Panadol is indicated for: Headache, Colds & Influenza, Backache, Period Pain, Pain of Osteoarthritis, " +
            "Muscle Pain, Toothache, Rheumatic Pain.";
    String url = "https://oneononepharmacy.com/wp-content/uploads/Panadol-Tab-24.jpg";


}