/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import facades.ProductFacade;
import facades.SalesFacade;
import facades.UserFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import models.Admin;
import models.Members;
import models.Products;
import models.Sales;

/**
 *
 * @author
 */
@ManagedBean
@SessionScoped
public class SalesManagedBean implements Serializable {

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @EJB
    SalesFacade sf;
    private Date start = new Date();
    private Date end = new Date();
    private List<Sales> ls = null;

    public List<Sales> getLs() {
        return ls;
    }

    public void setLs(List<Sales> ls) {
        this.ls = ls;
    }

    public void showSales() {
        this.ls = sf.fetchList(start, end);
    }

    public SalesFacade getSf() {
        return sf;
    }

    public void setSf(SalesFacade sf) {
        this.sf = sf;
    }

    public UserManagedBean getUserManagedBean() {
        return userManagedBean;
    }

    public void setUserManagedBean(UserManagedBean userManagedBean) {
        this.userManagedBean = userManagedBean;
    }

    public ProductFacade getProductFacade() {
        return productFacade;
    }

    public void setProductFacade(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    public SalesFacade getSalesFacade() {
        return salesFacade;
    }

    public void setSalesFacade(SalesFacade salesFacade) {
        this.salesFacade = salesFacade;
    }

    @ManagedProperty(value = "#{userManagedBean}")
    private UserManagedBean userManagedBean;

    @EJB
    ProductFacade productFacade;
    @EJB
    SalesFacade salesFacade;

    @EJB
    UserFacade userFacade;

    public void createSale(Sales sale) {
        //-- sale
        this.salesFacade.createSale(sale);
        //-- update Product
        Products product = sale.getProductId();
        product.setAvailableQuantity(product.getAvailableQuantity() - sale.getQuantity());
        this.productFacade.updateProduct(product);
        //--
        double profitOwner = (sale.getPrice() * sale.getQuantity()) / 3;
        double profitAdmin = (sale.getPrice() * sale.getQuantity()) / 3;
        double profitMembers = (sale.getPrice() * sale.getQuantity()) / 3;
        //-- Profit owner
        Members ownerMember = this.userManagedBean.findMemberOwner(product.getId());
        if (ownerMember == null) {
            System.out.println("------------------> There is no owner for this product");
            return;
        }
        ownerMember.setProfit(profitOwner);
        this.userFacade.updateMember(ownerMember);

        //-- Profit Admin
        Admin admin = this.userFacade.findAdmin();
        admin.setProfit(admin.getProfit() + profitAdmin);
        this.userFacade.updateAdmin(admin);
        //-- For all members
        List<Members> allMebers = this.userFacade.getAllMembers();
        for (Members m : allMebers) {
            if (m.getVerifiedByAdmin() == (short) 1) {
                double profitTemp = profitMembers / allMebers.size();
                m.setProfit(m.getProfit() + profitTemp);
                this.userFacade.updateMember(m);
            }
        }
    }

}
