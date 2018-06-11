/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import models.Products;

/**
 *
 * @author
 */
@ManagedBean
@SessionScoped
public class ProductFilterManagedBean implements Serializable {

    private double lowerPriceValue;
    private double upperPriceValue;
    private String searchFilter;

    public String getSearchFilter() {
        return searchFilter;
    }

    public void setSearchFilter(String searchFilter) {
        this.searchFilter = searchFilter;
    }

    public double getLowerPriceValue() {
        return lowerPriceValue;
    }

    public void setLowerPriceValue(double lowerPriceValue) {
        this.lowerPriceValue = lowerPriceValue;
    }

    public double getUpperPriceValue() {
        return upperPriceValue;
    }

    public void setUpperPriceValue(double upperPriceValue) {
        this.upperPriceValue = upperPriceValue;
    }

    @ManagedProperty(value = "#{productManagedBean}")
    private ProductManagedBean productManagedBean;

    private List<Products> products;

    public ProductManagedBean getProductManagedBean() {
        return productManagedBean;
    }

    public void setProductManagedBean(ProductManagedBean productManagedBean) {
        this.productManagedBean = productManagedBean;
    }

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    @PostConstruct
    public void init() {
        this.products = this.productManagedBean.getProducts();
        //this.searchFilter = "";
    }

    //-- Fetch Product by category filter
    public void fetchProductByCategory() {
        //-- Find the category id from parameter
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        int categoryIdTemp = Integer.parseInt(params.get("categoryId"));
        if (categoryIdTemp == 0) {
            this.products = productManagedBean.getProducts();
            return;
        }
        List<Products> productsTemp = new ArrayList<Products>();
        for (Products product : this.productManagedBean.getProducts()) {
            if (product.getCategoryId().getId() == categoryIdTemp) {
                productsTemp.add(product);
            }
        }
        this.products.clear();
        this.products = productsTemp;
    }

    //-- Fetch Products by price filter
    public void fetchProductByPrice() {
        List<Products> productsTemp = new ArrayList<Products>();
        for (Products product : this.productManagedBean.getProducts()) {
            if (product.getPriceSale() >= this.lowerPriceValue && product.getPriceSale() <= this.upperPriceValue) {
                productsTemp.add(product);
            }
        }
        this.products.clear();
        this.products = productsTemp;
    }

    //-- Fetch Products by search filter
    public void fetchProductsBySearch() {

        //-- The situation where the search string is empty
        if (this.searchFilter == "" || this.searchFilter == null) {
            this.products.clear();
            this.products = this.productManagedBean.getProducts();
        } else {
            List<Products> productsTemp = new ArrayList<Products>();
            for (Products product : this.productManagedBean.getProducts()) {
                if (product.getName().toLowerCase().startsWith(this.searchFilter.toLowerCase())) {
                    productsTemp.add(product);
                }
            }
            this.products.clear();
            this.products = productsTemp;
        }

    }

}
