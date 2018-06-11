/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author george
 */
@Entity
@Table(name = "product_offer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductOffer.findAll", query = "SELECT p FROM ProductOffer p")
    , @NamedQuery(name = "ProductOffer.findById", query = "SELECT p FROM ProductOffer p WHERE p.id = :id")
    , @NamedQuery(name = "ProductOffer.findByQuantity", query = "SELECT p FROM ProductOffer p WHERE p.quantity = :quantity")
    , @NamedQuery(name = "ProductOffer.findByVerified", query = "SELECT p FROM ProductOffer p WHERE p.verified = :verified")})
public class ProductOffer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity")
    private int quantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "verified")
    private short verified;
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Members memberId;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Products productId;

    public ProductOffer() {
    }

    public ProductOffer(Integer id) {
        this.id = id;
    }

    public ProductOffer(Integer id, int quantity, short verified) {
        this.id = id;
        this.quantity = quantity;
        this.verified = verified;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public short getVerified() {
        return verified;
    }

    public void setVerified(short verified) {
        this.verified = verified;
    }

    public Members getMemberId() {
        return memberId;
    }

    public void setMemberId(Members memberId) {
        this.memberId = memberId;
    }

    public Products getProductId() {
        return productId;
    }

    public void setProductId(Products productId) {
        this.productId = productId;
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
        if (!(object instanceof ProductOffer)) {
            return false;
        }
        ProductOffer other = (ProductOffer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.ProductOffer[ id=" + id + " ]";
    }

}
