/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converters;

import facades.ProductFacade;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import models.Products;

/**
 *
 * @author
 */
public class productsConverter implements Converter {

    @EJB
    ProductFacade pf;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Products p = pf.getEm().find(Products.class, Integer.parseInt(value));
        return p;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Products p = (Products) value;
        return String.valueOf(p.getId());
    }

}
