/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.converter;

import com.lapool.bean.EditSystemUserBean;
import com.lapool.model.Channel;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author akshat666
 */
@ManagedBean
@RequestScoped
public class ChannelConverter implements Converter {

    @ManagedProperty(value = "#{editSystemUserBean}")
    private EditSystemUserBean editSystemUserBean;

    public void setEditSystemUserBean(EditSystemUserBean editSystemUserBean) {
        this.editSystemUserBean = editSystemUserBean;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && value.trim().length() > 0) {

            for (Channel chn : editSystemUserBean.getChannels()) {
                if (chn.getId() == Long.valueOf(value)) {
                    return chn;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value
    ) {

        if (value != null) {
            return String.valueOf(((Channel) value).getId());
        } else {
            return null;
        }
    }

}
