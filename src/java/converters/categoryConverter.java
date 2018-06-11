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
import javax.faces.bean.ManagedBean;
import models.ProductCategory;

/**
 *
 * @author
 */
@ManagedBean(name = "categoryConverter")
public class categoryConverter implements Converter {

    @EJB
    ProductFacade pf;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        ProductCategory pc = pf.getEm().find(ProductCategory.class, Integer.parseInt(value));
        return pc;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        ProductCategory pc = (ProductCategory) value;
        return String.valueOf(pc.getId());
    }

}
