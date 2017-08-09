package com.omidbiz.core.component;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import com.omidbiz.core.converter.jalali.JalaliDateConverter;
import com.omidbiz.core.converter.jalali.PersianDateConverter;

@FacesRenderer(componentFamily = JalaliCalendar.COMPONENT_FAMILY, rendererType = JalaliCalendarRenderer.RENDERER_TYPE)
public class JalaliCalendarRenderer extends Renderer
{

    public static final String RENDERER_TYPE = "com.omidbiz.faces.JalaliCalendarRenderer";

    @Override
    public void decode(FacesContext context, UIComponent component)
    {
        JalaliCalendar calendar = (JalaliCalendar) component;
        String param = calendar.getClientId(context);
        String submittedValue = context.getExternalContext().getRequestParameterMap().get(param);

        if (submittedValue != null)
        {
            calendar.setSubmittedValue(submittedValue);
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        if(component.isRendered())
        {
            this.encodeMarkup(context, (JalaliCalendar) component);
            this.encodeScript(context, (JalaliCalendar) component);
        }
    }

    private void encodeMarkup(FacesContext context, JalaliCalendar jalaliCalendar) throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = jalaliCalendar.getClientId(context);
        String forceId = jalaliCalendar.getForceId();
        if(forceId != null && forceId.trim().length() > 0)
            clientId = forceId;
        writer.startElement("input", jalaliCalendar);
        writer.writeAttribute("type", "text", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);
        //
        Object value = jalaliCalendar.getValue();
        if(value != null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat();
            if(jalaliCalendar.isShowTime())
                sdf.applyPattern("yyyy/MM/dd HH:mm");
            else
                sdf.applyPattern("yyyy/MM/dd");            
            writer.writeAttribute("value", PersianDateConverter.getInstance().GregorianToSolar(sdf.format(value)), null);
        }
        String style = jalaliCalendar.getStyle();
        if (style != null)
        {
            writer.writeAttribute("style", style, null);
        }
        boolean disabled = jalaliCalendar.isDisabled();
        if (disabled)
        {
            writer.writeAttribute("disabled", disabled, null);
        }
        String styleClass = jalaliCalendar.getStyleClass();
        if (styleClass != null)
        {
            writer.writeAttribute("class", styleClass, null);
        }

        writer.endElement("input");

    }

    private void encodeScript(FacesContext context, JalaliCalendar component) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        String clientId = component.getClientId(context);
        sb.append("<script>");
        sb.append("jQuery(document).ready(function(){");
        sb.append(String.format("jQuery('#%s')", clientId.replace(":", "\\\\:")));
        if (component.isShowTime())
            sb.append(".datetimepicker({controlType: 'select', timeFormat: 'HH:mm',oneLine: true, ");
        else
            sb.append(".datepicker({ ");
        sb.append(" isRTL:true, dateFormat: 'yy/mm/dd', showButtonPanel: true, changeMonth: true, changeYear: true ");
        if (component.getOnselect() != null && component.getOnselect().length() > 0)
            sb.append(String.format(", onSelect: function(v, c){ %s(v, c) }", component.getOnselect()));
        sb.append("}); ");
        sb.append("});");
        sb.append("</script>");
        ResponseWriter writer = context.getResponseWriter();
        writer.write(sb.toString());
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object value) throws ConverterException
    {

        JalaliCalendar calendar = (JalaliCalendar) component;
        String submittedValue = (String) value;

        if (submittedValue != null)
        {

            // Delegate to user supplied converter if defined
            try
            {
                Converter converter = calendar.getConverter();
                if (converter != null)
                {
                    return converter.getAsObject(context, calendar, submittedValue);
                }
            }
            catch (ConverterException e)
            {
                throw e;
            }

            JalaliDateConverter jalaliDateConverter = new JalaliDateConverter();
            return jalaliDateConverter.getAsObject(context, calendar, submittedValue);

        }
        return null;

    }
}
