package com.omidbiz.core.converter.jalali;

import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class JalaliDateConverter implements Converter
{

    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        if (value == null || value.length() == 0)
        {
            return null;
        }

        return PersianDateConverter.getInstance().SolarToGregorianAsDate(value);
    }

    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if (value == null)
        {
            return null;
        }
        return PersianDateConverter.getInstance().GregorianToSolar((Date) value, true);
    }

}
