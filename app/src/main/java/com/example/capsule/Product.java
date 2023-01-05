package com.example.capsule;

import android.net.Uri;

public class Product {
    private Uri productUri;
    private String productName, productID, productShortDescription, productLongDescription;
    private String productPrice;
    private boolean isExpanded;

    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Uri getProductImageUri() {
        return productUri;
    }

    public void setProductImageUri(Uri productUri) {
        this.productUri = productUri;
    }

    public Product(String productID, String productName, String productShortDescription, String productLongDescription, String productPrice, String imageUrl) {
        this.productID = productID;
        this.productName = productName;
        this.productShortDescription = productShortDescription;
        this.productLongDescription = productLongDescription;
        this.productPrice = productPrice;
        this.imageUrl = imageUrl;
        isExpanded = false;
    }


    public Product() {
    }
    

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductShortDescription() {
        return productShortDescription;
    }

    public void setProductShortDescription(String productShortDescription) {
        this.productShortDescription = productShortDescription;
    }

    public String getProductLongDescription() {
        return productLongDescription;
    }

    public void setProductLongDescription(String productLongDescription) {
        this.productLongDescription = productLongDescription;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
}
