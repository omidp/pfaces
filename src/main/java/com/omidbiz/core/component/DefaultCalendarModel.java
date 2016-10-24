package com.omidbiz.core.component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DefaultCalendarModel implements CalendarModel
{

    private static final PersianCalendar pc = new PersianCalendar();
    
    private List<CalendarEvent> events;

    public DefaultCalendarModel()
    {
        events = new ArrayList<CalendarEvent>();
    }

    public DefaultCalendarModel(List<CalendarEvent> events)
    {
        this.events = events;
    }

    public boolean isHoliday(Date date)
    {
        pc.setTime(date);       
        return pc.get(PersianCalendar.DAY_OF_WEEK) == (PersianCalendar.FRIDAY-1);
    }

    public void addEvent(CalendarEvent ce)
    {
        ce.setId(UUID.randomUUID().toString());
        this.events.add(ce);
    }

    public List<CalendarEvent> getEvents()
    {
        return this.events;
    }

}
