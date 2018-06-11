/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import Helper.mailsend;
import facades.ProductFacade;
import facades.WishlistFacade;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import models.ProductCategory;
import models.Products;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import models.Members;
import models.ProductOffer;
import models.Wishlist;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author
 */
@ManagedBean
@SessionScoped
public class ProductManagedBean implements Serializable {

    //+-----------------------------------------------+
    //|             EJB properties                    |
    //+-----------------------------------------------+
    @EJB
    ProductFacade productFacade;

    public ProductFacade getProductFacade() {
        return productFacade;
    }

    public void setProductFacade(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @EJB
    WishlistFacade wishlistFacade;

    public WishlistFacade getWishlistFacade() {
        return wishlistFacade;
    }

    public void setWishlistFacade(WishlistFacade wishlistFacade) {
        this.wishlistFacade = wishlistFacade;
    }

    //+-----------------------------------------------+
    //|             ManagedBean properties            |
    //+-----------------------------------------------+
    @ManagedProperty(value = "#{userManagedBean}")
    private UserManagedBean userManagedBean;

    public UserManagedBean getUserManagedBean() {
        return userManagedBean;
    }

    public void setUserManagedBean(UserManagedBean userManagedBean) {
        this.userManagedBean = userManagedBean;
    }

    //+-----------------------------------------------+
    //|             PostConstruct                     |
    //+-----------------------------------------------+
    @PostConstruct
    public void init() {
        cat = this.productFacade.fetchCatList();
        this.products = this.productFacade.getAllProducts();
    }

    //+-----------------------------------------------+
    //|             Properties                        |
    //+-----------------------------------------------+
    private List<Products> products;
    private List<Products> productsRelatedList;
    private List<Products> productsRelatedListReverse;
    private Products productDetails;
    private Products product;
    private String image;
    private List<ProductCategory> cat;
    private int productOfferSelectedId;
    private String name;
    private double price;
    private double multiplier;
    private int quantity;
    private ProductCategory category;
    private int productOfferQuantity;

    //+-----------------------------------------------+
    //|             Setters and Getters               |
    //+-----------------------------------------------+
    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public List<ProductCategory> getCat() {
        return cat;
    }

    public void setCat(List<ProductCategory> cat) {
        this.cat = cat;
    }

    public int getProductOfferSelectedId() {
        return productOfferSelectedId;
    }

    public void setProductOfferSelectedId(int productOfferSelectedId) {
        this.productOfferSelectedId = productOfferSelectedId;
    }

    public int getProductOfferQuantity() {
        return productOfferQuantity;
    }

    public void setProductOfferQuantity(int productOfferQuantity) {
        this.productOfferQuantity = productOfferQuantity;
    }

    public Products getProductDetails() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (params.size() <= 0) {
            return this.productDetails;
        }
        Integer productIdTemp = Integer.parseInt(params.get("productId"));
        this.productDetails = this.productFacade.getProductById(productIdTemp);
        return this.productDetails;
    }

    public void setProductDetails(Products productDetails) {
        this.productDetails = productDetails;
    }

    public List<Products> getProducts() {
        this.products = productFacade.getAllProducts();
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public List<Products> getProductsRelatedListReverse() {
        List<Products> productsRelatedListTemp = new ArrayList<>();
        for (Products productTemp : this.products) {
            if (this.getProductDetails().getId() != productTemp.getId()) {
                continue;
            }
            productsRelatedListTemp.add(productTemp);
        }
        return productsRelatedListReverse = productsRelatedListTemp;
    }

    public void setProductsRelatedListReverse(List<Products> productsRelatedListReverse) {
        this.productsRelatedListReverse = productsRelatedListReverse;
    }

    public List<Products> getProductsRelatedList() {
        List<Products> productsRelatedListTemp = new ArrayList<>();
        for (Products productTemp : this.products) {
            if (this.getProductDetails().getId() == productTemp.getId()
                    || this.getProductDetails().getCategoryId().getId() != productTemp.getCategoryId().getId()) {
                continue;
            }
            productsRelatedListTemp.add(productTemp);
        }
        return this.productsRelatedList = productsRelatedListTemp;
    }

    public void setProductsRelatedList(List<Products> productsRelatedList) {
        this.productsRelatedList = productsRelatedList;
    }

    //+-----------------------------------------------+
    //|             Additional Methods                |
    //+-----------------------------------------------+
    public List<ProductCategory> getAllCategories() {
        List<ProductCategory> categories = productFacade.getAllProductCategories();
        return categories;
    }

    public void addProductOffer() {
        ProductOffer offer = new ProductOffer();
        offer.setVerified((short) 0);
        offer.setQuantity(this.productOfferQuantity);
        int userId = this.userManagedBean.getUser().getId();
        Members memberTemp = this.userManagedBean.getUserFacade().findMember(userId);
        offer.setMemberId(memberTemp);
        Products product = productFacade.getProductById(this.productOfferSelectedId);
        offer.setProductId(product);
        boolean result = this.productFacade.addProductOffer(offer);

        FacesMessage message = new FacesMessage("Succesful", "The product offer has been made!");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void productOfferVerify(ProductOffer productOffer) throws AddressException {
        productOffer.setVerified((short) 1);
        Products product = productOffer.getProductId();
        product.setAvailableQuantity(product.getAvailableQuantity() + productOffer.getQuantity());
        this.productFacade.updateProduct(product);
        this.productFacade.updateProductOffer(productOffer);

        mailsend ms = new mailsend();
        String message = "Hello " + productOffer.getMemberId().getUserId().getName() + " your offer of " + productOffer.getQuantity()
                + " Kg of " + productOffer.getProductId().getName() + " has been verified";
        try {
            ms.sendmail("giraklidis@yahoo.com", message);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        List<Wishlist> lw = this.wishlistFacade.getClientProducts(product);

        for (Wishlist w : lw) {
            try {
//                ms.sendmail(w.getClientId().getUserId().getEmail(), "The items in your wishlist has just arrived");
                ms.sendmail("giraklidis@yahoo.com", "The items in your wishlist has just arrived");
            } catch (MessagingException ex) {
                ex.printStackTrace();
            }
            w.setNotified(Boolean.TRUE);
            this.wishlistFacade.Update(w);
        }
    }

    public Products getProductById(int productId) {
        Products productResult = null;
        for (Products product : this.products) {
            if (product.getId() == productId) {
                productResult = product;
            }
        }
        return productResult;
    }

    public List<Products> fetchProductList() {
        return this.productFacade.getProductList();
    }

    public List<ProductOffer> fetchProductListOffer() {
        List<ProductOffer> offers = this.productFacade.getProductListOffer();
        List<ProductOffer> offersTemp = new ArrayList<ProductOffer>();
        for (ProductOffer offer : offers) {
            if (offer.getVerified() == (short) 0) {
                offersTemp.add(offer);
            }
        }
        return offersTemp;
    }

    public List<ProductOffer> fetchProductListMember() {
        List<ProductOffer> productMemberTemp = new ArrayList<ProductOffer>();
        if (this.userManagedBean.getUser() == null) {
            return productMemberTemp;
        }
        productMemberTemp = this.productFacade.getProductListMember(this.userManagedBean.getUser().getId());
        return productMemberTemp;
    }

    public void editProduct(Products product) {
        this.product = product;
    }

    public void updateProduct(Products product) {
        this.productFacade.updateProduct(product);
    }

    public void saveProduct() {
        product = new Products();
        product.setName(name);
        product.setPriceSale(price);
        product.setPriceMul(multiplier);
        product.setAvailableQuantity(quantity);
        product.setCategoryId(category);
        product.setEnabled(true);
        product.setDescription("Empty description");
        product.setImage("images/products/dummyImg.jpg");
        this.productFacade.addProduct(product);
    }

    public void handleImgUpload(FileUploadEvent event) {

        DateTime dt = new DateTime();
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd-MM-yyyy-HH-mm-ss");

        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/images/products");//"F:/Computer Science/NetBean Projects/wildfly-11.0.0.Final/static/images";//

        String newname = this.product.getName() + dt.toString(dtf) + "." + event.getFile().getContentType().toString().substring(6);
        Path destination = Paths.get(path, newname);
        InputStream file = null;
        try {
            file = event.getFile().getInputstream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (null != file) {
            try {
                Files.copy(file, destination);
                product.setImage("images/products/" + newname);
                this.productFacade.updateProduct(product);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

}
