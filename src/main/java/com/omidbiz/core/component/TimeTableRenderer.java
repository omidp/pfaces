package com.omidbiz.core.component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;
import javax.servlet.http.HttpServletRequest;

import com.omidbiz.core.converter.jalali.PersianDateConverter;

@FacesRenderer(componentFamily = TimeTable.COMPONENT_FAMILY, rendererType = TimeTableRenderer.RENDERER_TYPE)
public class TimeTableRenderer extends Renderer
{

    public static final String RENDERER_TYPE = "com.omidbiz.faces.TimeTableRenderer";

    private static final PersianCalendar pc = new PersianCalendar();

    public static final String[] DAYS_NAMES = new String[] { "\u0634", "\u06CC", "\u062F", "\u0633", "\u0686", "\u067E", "\u062C" };
    public static final int[] DAYS_IN_MONTH = { 31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29 };

    public static final String MONTH_PARAM = "javax.faces.timetable.month";

    public static final String YEAR_PARAM = "javax.faces.timetable.year";

    private static final SimpleDateFormat sdf = new SimpleDateFormat();

    @Override
    public void decode(FacesContext context, UIComponent component)
    {
        TimeTable calendar = (TimeTable) component;
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
        this.encodeMarkup(context, (TimeTable) component);
        this.encodeScript(context, (TimeTable) component);
    }

    private void encodeMarkup(FacesContext context, TimeTable timeTable) throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = timeTable.getClientId(context);
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
        String monthParam = req.getParameter(MONTH_PARAM);
        String yearParam = req.getParameter(YEAR_PARAM);
        //
        if (monthParam != null && monthParam.trim().length() > 0)
            pc.set(PersianCalendar.MONTH, Integer.parseInt(monthParam));
        if (yearParam != null && yearParam.trim().length() > 0)
            pc.set(PersianCalendar.YEAR, Integer.parseInt(yearParam));

        //
        StringBuilder sb = new StringBuilder();
        encodeHeader(context, timeTable, sb);
        encodeTable(context, timeTable, sb);

        writer.append(sb.toString());
    }

    private void encodeTable(FacesContext context, TimeTable timeTable, StringBuilder tt)
    {
        tt.append("<table dir='rtl' class=\"ptimetable bordered\">");
        tt.append("<thead>").append("<tr>");
        // build days header
        for (int i = 0; i < DAYS_NAMES.length; i++)
        {
            tt.append("<th>").append(DAYS_NAMES[i]).append("</th>");
        }
        tt.append("</tr>").append("</thead>");
        // build tbody days
        if (PersianCalendar.isLeapYear(pc.get(PersianCalendar.YEAR)))
            DAYS_IN_MONTH[11] = 30;
        tt.append("<tbody>");
        int monthStartDay = pc.get(PersianCalendar.DAY_OF_WEEK)-1;
        int month = pc.get(PersianCalendar.MONTH);  // based on zero
        int monthDay = DAYS_IN_MONTH[month];
        int dayCounter = 0;
        for (int i = 1; i <= (monthDay + monthStartDay); i++)
        {
            if (dayCounter == 0)
                tt.append("<tr>");
            if (i <= monthStartDay)
                tt.append("<td>").append(" ").append("</td>");
            else
            {
                tt.append("<td>").append(i - monthStartDay).append("</td>");
            }
            dayCounter++;

            if (dayCounter % 7 == 0)
            {
                dayCounter = 0;
                tt.append("</tr>");
            }
        }
        //
        tt.append("</tr>");
        tt.append("</tbody>").append("</table>");
    }

    private void encodeHeader(FacesContext context, TimeTable timeTable, StringBuilder content)
    {
        sdf.applyPattern("yyyy/MM/dd");
        UIViewRoot viewRoot = context.getViewRoot();
        String viewId = viewRoot.getViewId();        
        Map<String, List<String>> parameters = new HashMap<String, List<String>>();
        //
        content.append("<div class=\"row\">");
        
        content.append("<div class=\"col\">");
        parameters.put(YEAR_PARAM, Arrays.asList(String.valueOf(pc.get(PersianCalendar.YEAR)-1)));
        String url = context.getApplication().getViewHandler().getBookmarkableURL(context, viewId, parameters, false);
        content.append(String.format("<a href='%s' class='prevYearLink'></a>", url));
        content.append("</div>");
        //
        content.append("<div class=\"col\">");
        parameters.clear();
        parameters.put(MONTH_PARAM, Arrays.asList(String.valueOf(pc.get(PersianCalendar.MONTH)-1)));
        url = context.getApplication().getViewHandler().getBookmarkableURL(context, viewId, parameters, false);
        content.append(String.format("<a href='%s' class='prevMonthLink'></a>", url));        
        content.append("</div>");
        //
        content.append("<div class=\"col\">").append(PersianDateConverter.getInstance().GregorianToSolar(sdf.format(pc.getTime()))).append("</div>");
        //
        parameters.clear();
        content.append("<div class=\"col\">");
        parameters.put(YEAR_PARAM, Arrays.asList(String.valueOf(pc.get(PersianCalendar.YEAR)+1)));
        url = context.getApplication().getViewHandler().getBookmarkableURL(context, viewId, parameters, false);
        content.append(String.format("<a href='%s' class='nextYearLink'></a>", url));
        content.append("</div>");
        content.append("<div class=\"col\">");
        parameters.clear();
        parameters.put(MONTH_PARAM, Arrays.asList(String.valueOf(pc.get(PersianCalendar.MONTH)+1)));
        url = context.getApplication().getViewHandler().getBookmarkableURL(context, viewId, parameters, false);
        content.append(String.format("<a href='%s' class='nextMonthLink'></a>", url));        
        content.append("</div>");
        //
        content.append("</div>");
    }

    private void encodeScript(FacesContext context, TimeTable component) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        String clientId = component.getClientId(context);
        sb.append("<script>");
        sb.append("jQuery(document).ready(function(){");
        sb.append(String.format("jQuery('#%s')", clientId.replace(":", "\\\\:")));

        sb.append("});");
        sb.append("</script>");
        ResponseWriter writer = context.getResponseWriter();
        writer.write(sb.toString());
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object value) throws ConverterException
    {

        TimeTable calendar = (TimeTable) component;
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

        }
        return null;

    }

    public static void main(String[] args)
    {
        pc.set(PersianCalendar.MONTH, 7);
      System.out.println( pc.get(PersianCalendar.DAY_OF_WEEK));
//        PersianCalendar pc = new PersianCalendar();
//        StringBuilder tt = new StringBuilder();
//        tt.append("<table dir='rtl'>");
//        tt.append("<thead>").append("<tr>");
//        // build days
//        for (int i = 0; i < DAYS_NAMES.length; i++)
//        {
//            tt.append("<th>").append(DAYS_NAMES[i]).append("</th>");
//        }
//        tt.append("</tr>").append("</thead>");
//        tt.append("<tbody>");
//        // build month days
//        DAYS_IN_MONTH[11] = 30;
//        int monthStartDay = pc.get(PersianCalendar.DAY_OF_MONTH);
//        int month = pc.get(PersianCalendar.MONTH);// get from calendar start
//                                                  // based on zero
//        int monthDay = DAYS_IN_MONTH[month];
//        int dayCounter = 0;
//        for (int i = 1; i <= (monthDay + monthStartDay); i++)
//        {
//            if (dayCounter == 0)
//                tt.append("<tr>");
//            if (i <= monthStartDay)
//                tt.append("<td>").append(" ").append("</td>");
//            else
//            {
//                tt.append("<td>").append(i - monthStartDay).append("</td>");
//            }
//            dayCounter++;
//
//            if (dayCounter % 7 == 0)
//            {
//                dayCounter = 0;
//                tt.append("</tr>");
//            }
//        }
//        //
//        tt.append("</tr>");
//        tt.append("</tbody>").append("</table>");
//        System.out.println(tt.toString());
    }

}
