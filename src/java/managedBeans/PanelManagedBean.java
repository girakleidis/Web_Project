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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import models.Products;
import models.Sales;
import models.Users;

/**
 *
 * @author
 */
@ManagedBean
@SessionScoped
public class PanelManagedBean implements Serializable {

    @ManagedProperty(value = "#{userManagedBean}")
    private UserManagedBean userManagedBean;

    //-- page incluide
    private String pageInclude;

    public String getPageInclude() {
        return pageInclude;
    }

    public void setPageInclude(String pageInclude) {
        System.out.print("---------->pageInclude == " + pageInclude);

        this.pageInclude = pageInclude;
        this.usersList = this.userFacade.getAllUser();
    }

    public UserManagedBean getUserManagedBean() {
        return userManagedBean;
    }

    public void setUserManagedBean(UserManagedBean userManagedBean) {
        this.userManagedBean = userManagedBean;
    }

    @PostConstruct
    public void init() {
        //this.setPageInclude("panelAdminNotification.xhtml");
        if (userManagedBean.getUser() == null) {
            return;
        }
        if (userManagedBean.getUser().getRoleId().getRole() == "admin") {
            this.setPageInclude("panelAdminNotification.xhtml"); // Ensure that default is been set.
        } else if (userManagedBean.getUser().getRoleId().getRole() == "member") {
            this.setPageInclude("panelMemberStatistics.xhtml"); // Ensure that default is been set.
        }
        this.usersList = this.userFacade.getAllUser();
        this.setProductList(this.productFacade.getAllProducts());
        this.salesList = new ArrayList<Sales>();
    }

    //-- For users
    @EJB
    private UserFacade userFacade;

    public UserFacade getUserFacade() {
        return userFacade;
    }

    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    private List<Users> usersList;

    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }

    public void deleteUser(int userId) {
        this.userFacade.deleteUser(userId);
        this.setUsersList(this.userFacade.getAllUser());
    }

    //-- For Products
    @EJB
    private ProductFacade productFacade;

    private List<Products> productList;

    public ProductFacade getProductFacade() {
        return productFacade;
    }

    public void setProductFacade(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    public List<Products> getProductList() {
        return productList;
    }

    public void setProductList(List<Products> productList) {
        this.productList = productList;
    }

    //-- For Sales
    @EJB
    private SalesFacade salesFacade;

    private Date saleStart;
    private Date saleEnd;

    public List<Sales> showSales() {
        return this.salesList;
    }

    public Date getSaleStart() {
        return saleStart;
    }

    public void setSaleStart(Date saleStart) {
        this.saleStart = saleStart;
    }

    public Date getSaleEnd() {
        return saleEnd;
    }

    public void setSaleEnd(Date saleEnd) {
        this.saleEnd = saleEnd;
    }

    private List<Sales> salesList;

    public SalesFacade getSalesFacade() {
        return salesFacade;
    }

    public void setSalesFacade(SalesFacade salesFacade) {
        this.salesFacade = salesFacade;
    }

    public List<Sales> getSalesList() {
        this.salesList = this.salesFacade.fetchList(this.saleStart, this.saleEnd);
        System.out.println(this.salesList.size());;
        return salesList;
    }

    public void setSalesList(List<Sales> salesList) {
        this.salesList = salesList;
    }

}
