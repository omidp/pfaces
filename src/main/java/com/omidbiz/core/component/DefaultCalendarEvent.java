package com.omidbiz.core.component;

import java.util.Date;

public class DefaultCalendarEvent implements CalendarEvent
{

    private String id;
    private String title;
    private Object data;
    private Date startDate;
    private Date endDate;

    public DefaultCalendarEvent(String id, String title, Object data, Date startDate, Date endDate)
    {
        this.id = id;
        this.title = title;
        this.data = data;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Object getData()
    {
        return data;
    }

    public String getTitle()
    {
        return title;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

}
