/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import models.Products;
import models.Wishlist;

/**
 *
 * @author
 */
@Stateless
public class WishlistFacade {

    @PersistenceContext
    private EntityManager em;

    public List<Wishlist> getClientProducts(Products product) {
        List<Wishlist> wl = null;
        try {
            wl = em.createNativeQuery("select * from wishlist where product_id= :pid and quantity <= :quan and notified = 0", Wishlist.class)
                    .setParameter("pid", product.getId())
                    .setParameter("quan", product.getAvailableQuantity())
                    .getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return wl;
    }

    public List<Wishlist> getAllWishList(int clientId) {
        //List<Wishlist> wishList = em.createNamedQuery("Wishlist.findAll", Wishlist.class).getResultList();
        List<Wishlist> wishList = em.createNativeQuery("select * from wishlist where client_id= :client", Wishlist.class)
                .setParameter("client", clientId)
                .getResultList();
        return wishList;
    }

    public void Update(Wishlist wl) {
        em.merge(wl);
        em.flush();
    }

    public void add(Wishlist wl) {
        em.persist(wl);
        em.flush();
    }

    public void remove(Wishlist wish) {
        Wishlist wishTemp = em.find(Wishlist.class, wish.getId());
        em.remove(wishTemp);
        em.flush();
    }
}
