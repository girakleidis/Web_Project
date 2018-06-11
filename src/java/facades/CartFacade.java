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
import models.Cart;

/**
 *
 * @author
 */
@Stateless
public class CartFacade {

    @PersistenceContext
    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public List<Cart> getCartList(int clientId) {
        List<Cart> cartTemp = em.createNativeQuery("SELECT * FROM cart WHERE client_id = :clientId", Cart.class)
                .setParameter("clientId", clientId)
                .getResultList();
        return (cartTemp.size() != 0) ? cartTemp : null;
    }

    public void insertProductToCart(Cart cart) {
        em.persist(cart);
        em.flush();
    }

    public void deleteFromCart(Cart cart) {
        Cart cartTemp = em.find(Cart.class, cart.getId());
        em.remove(cartTemp);
        em.flush();
    }

    public void updateCart(Cart cart) {
//        Cart cartTemp = em.find(Cart.class,cart.getId());
        em.merge(cart);
        em.flush();
    }

}
