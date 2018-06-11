/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import Helper.mailsend;
import facades.ProductFacade;
import facades.UserFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpSession;
import models.Members;
import models.ProductOffer;
import models.Role;
import models.Users;
import models.Wishlist;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author
 */
@ManagedBean
@SessionScoped
public class UserManagedBean implements Serializable {

    @EJB
    UserFacade userFacade;
    private List<Users> usersList;

    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = this.userFacade.getAllUser();
    }

    private Users user;
    private Users userRegister;
    private String passwordValid;

    public String getPasswordValid() {
        return passwordValid;
    }

    public void setPasswordValid(String passwordValid) {
        this.passwordValid = passwordValid;
    }

    public Users getUserRegister() {
        return userRegister;
    }

    public void setUserRegister(Users userRegister) {
        this.userRegister = userRegister;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public UserFacade getUserFacade() {
        return userFacade;
    }

    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @PostConstruct
    public void init() {
        this.user = new Users();
        this.userRegister = new Users();
        this.usersList = this.userFacade.getAllUser();
    }

    @ManagedProperty(value = "#{wishListManagedBean}")
    private WishListManagedBean wishListManagedBean;

    public WishListManagedBean getWishListManagedBean() {
        return wishListManagedBean;
    }

    public void setWishListManagedBean(WishListManagedBean wishListManagedBean) {
        this.wishListManagedBean = wishListManagedBean;
    }

    public String login() {
        //-- Password Validation
        Users userTemp = this.userFacade.matchUser(this.user);
        if (userTemp != null) {
            this.user = userTemp;
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
            session.setAttribute("user", userTemp);

            if (this.user.getRoleId().getRole().trim().equals("client")) {
                wishListManagedBean.setClient(this.userFacade.findClient(userTemp.getId()));
                wishListManagedBean.setWishList(null);
                return "index.xhtml";
            } else if (this.user.getRoleId().getRole().trim().equals("member")) {
                wishListManagedBean.setClient(null);
                wishListManagedBean.setWishList(new ArrayList<Wishlist>());
                return "/panel/panelMember/memberPanel.xhtml/login?faces-redirect=true";
            } else if (this.user.getRoleId().getRole().trim().equals("admin")) {
                wishListManagedBean.setClient(null);
                wishListManagedBean.setWishList(new ArrayList<Wishlist>());
                return "/panel/panelAdmin/adminPanel.xhtml/login?faces-redirect=true";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Login Failed. Please try again..."));
        }
        return "";
    }

    private int role;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String registerUser() throws AddressException, MessagingException {

        if (this.userRegister.getPassword().equals(this.passwordValid)) {
            //-- Set the rule
            Role role = new Role();
            role.setId(this.role);
            role.setRole((this.role == 3) ? "client" : "member");
            this.userRegister.setRoleId(role);
            this.userRegister.setPassword(BCrypt.hashpw(this.passwordValid, BCrypt.gensalt()));
            Users userTemp = this.userRegister;
            //-- Email Message
            String messageTemp = "Thank you for the registration on Cooperation site!\n "
                    + "Your username : " + userTemp.getEmail() + "\n"
                    + "Your Password : " + userTemp.getPassword();

            if (userTemp != null) {
                if (this.role == 3) {
                    boolean result = this.userFacade.registerUser(userTemp, "client");
                    if (!result) {
                        return "";
                    }
                    //                mailsend mail = new mailsend();
                    //                mail.sendmail(userTemp.getEmail(), messageTemp);
                } else if (this.role == 2) {
                    boolean result = this.userFacade.registerUser(userTemp, "member");
                    if (!result) {
                        return "";
                    }
                    //                mailsend mail = new mailsend();
                    //                mail.sendmail(userTemp.getEmail(), messageTemp);
                }

                return "login.xhtml";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Register Failed. Please try again..."));
        }
        return "";
    }

    public List<Members> fetchAllUnverifiedMembers() {
        List<Members> usersTemp = this.userFacade.getAllMembers();
        List<Members> membersTemp = new ArrayList<Members>();
        for (Members users : usersTemp) {
            if (users.getVerifiedByAdmin() == (short) 0) {
                membersTemp.add(users);
            }
        }
        return membersTemp;
    }

    public void memberVerify(Members member) {
        member.setVerifiedByAdmin((short) 1);
        this.userFacade.updateMember(member);
    }

    @EJB
    ProductFacade productFacade;

    public Members findMemberOwner(int productId) {
        List<ProductOffer> productOffers = this.productFacade.getProductListOffer();
        for (ProductOffer offer : productOffers) {
            if (offer.getProductId().getId().equals(productId)) {
                if (offer.getMemberId().getVerifiedByAdmin() == (short) 1) {
                    return offer.getMemberId();
                }
            }
        }
        return null;
    }

    public ProductFacade getProductFacade() {
        return productFacade;
    }

    public void setProductFacade(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    public double memberProfit() {
        double profit = 0;
        Members member = this.userFacade.findMember(this.user.getId());
        profit = member.getProfit();
        return profit;
    }
//NEW

    public String forgotPassword(String email) throws AddressException, MessagingException {
        Users user = userFacade.recoverPass(email);

        if (user != null) {
            String newpassword = UUID.randomUUID().toString();
            user.setPassword(BCrypt.hashpw(newpassword, BCrypt.gensalt()));
            System.out.println(newpassword);
            if (userFacade.updateUser(user) == true) {
                String messageTemp = "Your new password is " + newpassword;
                mailsend mail = new mailsend();
                mail.sendmail(user.getEmail(), messageTemp);
                return "login.xhtml";
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Something went wrong. Please try again..."));
                return "";
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Wrong email. Please try again..."));
            return "";
        }
    }

}
