/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import facades.WishlistFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import models.Clients;
import models.Products;
import models.Wishlist;

/**
 *
 * @author
 */
@ManagedBean
@SessionScoped
public class WishListManagedBean implements Serializable {

    private Clients client;

    public Clients getClient() {
        return client;
    }

    public void setClient(Clients client) {
        this.client = client;
    }

    private List<Wishlist> wishList;

    public List<Wishlist> getWishList() {
        return wishList;
    }

    public void setWishList(List<Wishlist> wishList) {
        if (wishList == null) {
            if (this.getClient() == null) {
                this.wishList = new ArrayList<Wishlist>();
                return;
            }
            this.wishList = this.wishlistFacade.getAllWishList(this.client.getId());
        } else {
            this.wishList = wishList;
        }
    }

    public void updateWishListOnLogOut() {
        this.wishList = new ArrayList<Wishlist>();
    }

    @EJB
    private WishlistFacade wishlistFacade;

    public WishlistFacade getWishlistFacade() {
        return wishlistFacade;
    }

    public void setWishlistFacade(WishlistFacade wishlistFacade) {
        this.wishlistFacade = wishlistFacade;
    }

    @PostConstruct
    public void init() {
        if (this.client == null) {
            this.wishList = new ArrayList<Wishlist>();
            return;
        }
        this.setWishList(null);
    }

    private int quantityWishList;

    public int getQuantityWishList() {
        return quantityWishList;
    }

    public void setQuantityWishList(int quantityWishList) {
        this.quantityWishList = quantityWishList;
    }

    //-- Add product to wishlist
    public boolean addProductWishList(Products product) {
        Clients client = this.getClient();
        if (client == null) {
            return false;
        }
        Wishlist wishListTemp = new Wishlist();
        wishListTemp.setProductId(product);
        wishListTemp.setQuantity(this.quantityWishList);
        wishListTemp.setNotified(true);
        wishListTemp.setClientId(client);
        this.wishlistFacade.add(wishListTemp);
        this.setWishList(null);
        return true;
    }

    //-- Delete
    public void deleteProductWishList(Wishlist wish) {
        this.wishlistFacade.remove(wish);
        this.setWishList(null);
    }

    //-- Delete All Button
    public void deleteAllProductWishList() {
        for (Wishlist wish : this.wishList) {
            this.wishlistFacade.remove(wish);
        }
        this.setWishList(null);
    }

    //-- Total Price
    public double getTotalPrice() {
        double priceTemp = 0;
        for (Wishlist wishListTemp : this.wishList) {
            priceTemp += wishListTemp.getProductId().getPriceSale();
        }
        return priceTemp;
    }

    public List<Wishlist> getWishList(Products product) {
        List<Wishlist> lw = null;
        lw = this.wishlistFacade.getClientProducts(product);
        return lw;
    }

    public void updateWishlist(Wishlist wl) {
        wl.setNotified(Boolean.TRUE);
    }
}
