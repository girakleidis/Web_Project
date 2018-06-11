/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import models.ProductCategory;
import models.ProductOffer;
import models.Products;

/**
 *
 * @author
 */
@Stateless
public class ProductFacade {

    @PersistenceContext
    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public List<Products> getAllProducts() {
        List<Products> products = em.createNamedQuery("Products.findAll").getResultList();
        return products;
    }

    public List<ProductOffer> getProductListOffer() {
        List<ProductOffer> products = em.createNamedQuery("ProductOffer.findAll").getResultList();
        return products;
    }

    public List<ProductCategory> getAllProductCategories() {
        List<ProductCategory> categories = em.createNamedQuery("ProductCategory.findAll").getResultList();
        return categories;
    }

    public Products getProductById(int productId) {
        List<Products> productList = em.createNamedQuery("Products.findById", Products.class).setParameter("id", productId).getResultList();
        Products product = (productList.size() > 0) ? productList.get(0) : null;
        return product;
    }

    public Products getProductByName(String productName) {
        List<Products> productList = em.createNamedQuery("Products.findByName", Products.class).setParameter("name", productName).getResultList();
        Products product = (productList.size() > 0) ? productList.get(0) : null;
        return product;
    }

    //-- george
    public List<Products> getProductList() {
        List<Products> productListTemp = em.createNamedQuery("Products.findAll", Products.class).getResultList();
        return productListTemp;
    }

    public List<ProductOffer> getProductListMember(int memberId) {
        List<ProductOffer> productListNew = new ArrayList<ProductOffer>();
        List<ProductOffer> productListTemp = em.createNamedQuery("ProductOffer.findAll", ProductOffer.class).getResultList();

        for (ProductOffer productOffer : productListTemp) {
            if (productOffer.getMemberId().getUserId().getId().equals(memberId)) {
                productListNew.add(productOffer);
            }
        }
        return productListNew;
    }

    public Products getProduct(int id) {
        Products product = (Products) em.createNamedQuery("Products.findById").setParameter("id", id).getSingleResult();
        return product;
    }

    public List<ProductCategory> fetchCatList() {
        List<ProductCategory> lpc = em.createNamedQuery("ProductCategory.findAll", ProductCategory.class).getResultList();
        return lpc;
    }

    public boolean addProduct(Products product) {
        try {
            em.persist(product);
            em.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean addProductOffer(ProductOffer product) {
        try {
            em.persist(product);
            em.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean updateProduct(Products product) {
        try {
            em.merge(product);
            em.flush();

        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }

        return true;
    }

    public boolean updateProductOffer(ProductOffer product) {
        try {
            em.merge(product);
            em.flush();

        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }

        return true;
    }
}
