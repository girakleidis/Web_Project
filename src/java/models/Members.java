/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author
 */
@Entity
@Table(name = "members")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Members.findAll", query = "SELECT m FROM Members m")
    , @NamedQuery(name = "Members.findByVerified", query = "SELECT m FROM Members m WHERE m.verified = :verified")
    , @NamedQuery(name = "Members.findByVerifiedByAdmin", query = "SELECT m FROM Members m WHERE m.verifiedByAdmin = :verifiedByAdmin")
    , @NamedQuery(name = "Members.findById", query = "SELECT m FROM Members m WHERE m.id = :id")
    , @NamedQuery(name = "Members.findByProfit", query = "SELECT m FROM Members m WHERE m.profit = :profit")})
public class Members implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "verified")
    private short verified;
    @Basic(optional = false)
    @NotNull
    @Column(name = "verifiedByAdmin")
    private short verifiedByAdmin;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "profit")
    private Double profit;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "memberId")
    private Collection<ProductOffer> productOfferCollection;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users userId;

    public Members() {
    }

    public Members(Integer id) {
        this.id = id;
    }

    public Members(Integer id, short verified, short verifiedByAdmin) {
        this.id = id;
        this.verified = verified;
        this.verifiedByAdmin = verifiedByAdmin;
    }

    public short getVerified() {
        return verified;
    }

    public void setVerified(short verified) {
        this.verified = verified;
    }

    public short getVerifiedByAdmin() {
        return verifiedByAdmin;
    }

    public void setVerifiedByAdmin(short verifiedByAdmin) {
        this.verifiedByAdmin = verifiedByAdmin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    @XmlTransient
    public Collection<ProductOffer> getProductOfferCollection() {
        return productOfferCollection;
    }

    public void setProductOfferCollection(Collection<ProductOffer> productOfferCollection) {
        this.productOfferCollection = productOfferCollection;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Members)) {
            return false;
        }
        Members other = (Members) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Members[ id=" + id + " ]";
    }

}
