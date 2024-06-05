package edu.cinec.mygrocerystore;

public class CartProduct {
    String productId, productName, productDescription, productImage;

    double productPrice;
    int quantity;
    public String getProductId () {
        return productId;
    }
    public String getQuantity () {
        return Integer.toString(quantity);
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
}
