package com.omidbiz.core.component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DefaultCalendarModel implements CalendarModel
{

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
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        //
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);
        cal2.set(Calendar.HOUR, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        return cal2.getTime().compareTo(cal.getTime()) == 0;
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
