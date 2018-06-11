/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import facades.CartFacade;
import facades.SalesFacade;
import facades.UserFacade;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import models.Cart;
import models.Clients;
import models.Products;
import models.Sales;
import models.Users;

/**
 *
 * @author
 */
@ManagedBean
@SessionScoped
public class CartManagedBean implements Serializable {

    private List<Cart> cartList;
    private double cartTotal;
    private String cartProductHiddenId;

    public String getCartProductHiddenId() {
        return cartProductHiddenId;
    }

    public void setCartProductHiddenId(String cartProductHiddenId) {
        this.cartProductHiddenId = cartProductHiddenId;
    }

    public double getCartTotal() {
        return cartTotal;
    }

    public void setCartTotal(double cartTotal) {
        this.cartTotal = cartTotal;
    }

    @ManagedProperty(value = "#{userManagedBean}")
    private UserManagedBean userManagedBean;

    @ManagedProperty(value = "#{productManagedBean}")
    private ProductManagedBean productManagedBean;

    public ProductManagedBean getProductManagedBean() {
        return productManagedBean;
    }

    public void setProductManagedBean(ProductManagedBean productManagedBean) {
        this.productManagedBean = productManagedBean;
    }

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }

    public UserManagedBean getUserManagedBean() {
        return userManagedBean;
    }

    public void setUserManagedBean(UserManagedBean userManagedBean) {
        this.userManagedBean = userManagedBean;
    }

    @EJB
    private UserFacade userFacade;

    public UserFacade getUserFacade() {
        return userFacade;
    }

    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @EJB
    private CartFacade cartFacade;

    public CartFacade getCartFacade() {
        return cartFacade;
    }

    public void setCartFacade(CartFacade cartFacade) {
        this.cartFacade = cartFacade;
    }

    @PostConstruct
    public void init() {
        this.refreshCartList();
        this.quantitySale = 1;
        this.quantityHashMap = new HashMap<String, Integer>();
    }

    public void refreshCartList() {
        this.cartList = this.cartListFromFacade();
    }

    //-- Cart List with client id
    public List<Cart> cartListFromFacade() {
        Clients clientTemp = this.findClient();

        List<Cart> cartListTemp2 = new ArrayList<Cart>();
        List<Cart> cartListTemp = (clientTemp == null) ? cartListTemp2 : cartFacade.getCartList(clientTemp.getId());
        return cartListTemp;
    }

    public void insertToCart(Products product) {
        if (product == null) {
            product = this.productManagedBean.getProductDetails();
            System.out.println(product);
            System.out.println("ELAAAAAAAAAA NULL:L:LLLL");
        } else {
            System.out.println("not null---------------------");
        }
        Clients clientTemp = this.findClient();
        if (clientTemp == null) {
            return;
        }

        Cart cart = new Cart();
        cart.setProductId(product);
        cart.setClientId(clientTemp);
        cart.setQuantity(this.quantityPassed);

        cartFacade.insertProductToCart(cart);

        this.refreshCartList();
    }

    private int idProductDetails;

    public int getIdProductDetails() {
        return idProductDetails;
    }

    public void setIdProductDetails(int idProductDetails) {
        this.idProductDetails = idProductDetails;
    }

    public void insertToCartDetails() {
        Products product = this.productManagedBean.getProductFacade().getProductById(this.idProductDetails);
        if (product == null) {
            return;
        }

        Clients clientTemp = this.findClient();
        if (clientTemp == null) {
            return;
        }

        Cart cart = new Cart();
        cart.setProductId(product);
        cart.setClientId(clientTemp);
        cart.setQuantity((this.quantityPassed == 0) ? 1 : this.quantityPassed);

        cartFacade.insertProductToCart(cart);

        this.refreshCartList();
    }

    private int quantityPassed;

    public int getQuantityPassed() {
        return quantityPassed;
    }

    public void setQuantityPassed(int quantityPassed) {
        this.quantityPassed = quantityPassed;
    }

    public void deleteProductFromCart(Cart cartTemp) {
        cartFacade.deleteFromCart(cartTemp);
        this.refreshCartList();
    }

    public void updateCart(Cart cart) {
        this.cartFacade.updateCart(cart);
    }

    @EJB
    SalesFacade salesFacade;

    @ManagedProperty(value = "#{salesManagedBean}")
    private SalesManagedBean salesManagedBean;

    public void createSale() {
        for (Cart cart : this.cartList) {
            //-- find product
            Products product = cart.getProductId();
            //-- find client
            Clients client = this.findClient();
            if (client == null) {
                return;
            }
            //-- create a sale
            Sales sale = new Sales();
            sale.setClientId(client);
            sale.setProductId(product);
            sale.setPrice(product.getPriceSale());
            sale.setQuantity(cart.getQuantity());
            //-- Date
            Date date = new Date();
            sale.setDate(date);
            this.salesManagedBean.createSale(sale);
        }
    }

    public SalesFacade getSalesFacade() {
        return salesFacade;
    }

    public void setSalesFacade(SalesFacade salesFacade) {
        this.salesFacade = salesFacade;
    }

    public SalesManagedBean getSalesManagedBean() {
        return salesManagedBean;
    }

    public void setSalesManagedBean(SalesManagedBean salesManagedBean) {
        this.salesManagedBean = salesManagedBean;
    }

    public Clients findClient() {
        Users user = userManagedBean.getUser();
        if (user.getId() == null) {
            return null;
        }
        int userID = user.getId();
        Clients client = userFacade.findClient(userID);
        return client;
    }

    private int quantitySale;

    public int getQuantitySale() {
        return quantitySale;
    }

    public void setQuantitySale(int quantitySale) {
        this.quantitySale = quantitySale;
    }

    private HashMap<String, Integer> quantityHashMap;

    public HashMap<String, Integer> getQuantityHashMap() {
        return quantityHashMap;
    }

    public void setQuantityHashMap(HashMap<String, Integer> quantityHashMap) {
        this.quantityHashMap = quantityHashMap;
    }

}
