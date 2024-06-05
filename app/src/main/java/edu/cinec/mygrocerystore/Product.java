package edu.cinec.mygrocerystore;

public class Product {
    String productId, productName, productDescription, productImage, category;
    double productPrice;

    public String getProductId () {
        return productId;
    }
    public String getProductName () {
        return productName;
    }


    public String getProductPrice () {
        return Double.toString(productPrice);
    }


    public String getProductDescription () {
        return productDescription;
    }

    public String getProductImage() {
        return productImage;
    }
    public String getCategory () {
        return category;
    }
}
