package com.omidbiz.core.component;

import java.util.Date;

public interface CalendarEvent
{

    public String getId();

    public void setId(String id);

    public Object getData();

    public String getTitle();

    public Date getStartDate();

    public Date getEndDate();

}
