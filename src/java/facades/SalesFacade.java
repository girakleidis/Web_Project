/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import models.Sales;

/**
 *
 * @author
 */
@Stateless
public class SalesFacade {

    @PersistenceContext
    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public List<Sales> getAllSales() {
        List<Sales> sales = em.createNamedQuery("Sales.findAll", Sales.class).getResultList();
        return sales;
    }

    public void createSale(Sales sale) {
        em.persist(sale);
        em.flush();
    }

    public List<Sales> fetchList(Date start, Date end) {
        if (start == null) {
            return new ArrayList<Sales>();
        }
        try {
            List<Sales> sales = em.createNamedQuery("Sales.findByDates", Sales.class)
                    .setParameter("start", new java.sql.Timestamp(start.getTime()))
                    .setParameter("end", new java.sql.Timestamp(end.getTime()))
                    .getResultList();
            return sales;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

}
