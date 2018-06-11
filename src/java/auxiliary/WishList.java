/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auxiliary;

import java.sql.Blob;

/**
 *
 * @author
 */
public class WishList {

    private String productName;
    private double productPrice;
    private Blob productImage;
    private int productId;

    public WishList(String productName, double productPrice, Blob productImage, int productID) {
        this.setProductName(productName);
        this.setProductPrice(productPrice);
        this.setProductImage(productImage);
        this.setProductId(productID);
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public Blob getProductImage() {
        return productImage;
    }

    public void setProductImage(Blob productImage) {
        this.productImage = productImage;
    }

}
