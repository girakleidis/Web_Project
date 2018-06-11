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
import models.Admin;
import models.Clients;
import models.Members;
import models.Users;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author
 */
@Stateless
public class UserFacade {

    @PersistenceContext
    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public Users matchUser(Users user) {
        Users userResult = null;
        String email = user.getEmail();
        String password = user.getPassword();
        try {
            userResult = em.createNamedQuery("Users.findByEmail", Users.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userResult != null && !BCrypt.checkpw(password, userResult.getPassword())) {
            userResult = null;
        }
        return userResult;
    }

    public Clients findClient(int userID) {
        List<Clients> clientTemp = em.createNativeQuery("SELECT * FROM clients WHERE user_id = :userID", Clients.class)
                .setParameter("userID", userID)
                .getResultList();
        return (clientTemp.size() != 0) ? clientTemp.get(0) : null;
    }

    public Members findMember(int userID) {
        List<Members> memberTemp = em.createNativeQuery("SELECT * FROM members WHERE user_id = :userID", Members.class)
                .setParameter("userID", userID)
                .getResultList();
        return (memberTemp.size() != 0) ? memberTemp.get(0) : null;
    }

    public boolean registerUser(Users user, String userType) {

        if (userType == "client") {
            try {
                em.persist(user);
                em.flush();
                Users userTemp = em.createNamedQuery("Users.findByName", Users.class).setParameter("name", user.getName()).getSingleResult();
                Clients client = new Clients();
                client.setUserId(userTemp);
                client.setVerified((short) 0);
                em.persist(client);
                em.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (userType == "member") {
            try {
                em.persist(user);
                em.flush();
                Users userTemp = em.createNamedQuery("Users.findByName", Users.class).setParameter("name", user.getName()).getSingleResult();
                Members member = new Members();
                member.setUserId(userTemp);
                member.setVerified((short) 0);
                member.setVerifiedByAdmin((short) 0);
                member.setProfit(0.0);
                em.persist(member);
                em.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public List<Users> getAllUser() {
        List<Users> usersTemp = new ArrayList<Users>();
        usersTemp = em.createNamedQuery("Users.findAll", Users.class).getResultList();
        return usersTemp;
    }

    public List<Members> getAllMembers() {
        List<Members> membersTemp = new ArrayList<Members>();
        membersTemp = em.createNamedQuery("Members.findAll", Members.class).getResultList();
        return membersTemp;
    }

    public void deleteUser(int userId) {
        Users user = em.find(Users.class, userId);
        em.remove(user);
        em.flush();
    }

    public void updateMember(Members member) {
        try {
            em.merge(member);
            em.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAdmin(Admin admin) {
        try {
            em.merge(admin);
            em.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//NEW

    public Users recoverPass(String email) {
        Users Result = null;
        try {
            Result = em.createNamedQuery("Users.findByEmail", Users.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result;
    }

    //NEW
    public Boolean updateUser(Users user) {
        try {
            em.merge(user);
            em.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Admin findAdmin() {
        List<Users> users = this.getAllUser();
        Admin admin = this.em.createNamedQuery("Admin.findAll", Admin.class).getSingleResult();
        return admin;
    }
}
