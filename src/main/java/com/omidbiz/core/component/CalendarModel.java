package com.omidbiz.core.component;

import java.util.Date;
import java.util.List;

public interface CalendarModel
{

    public boolean isHoliday(Date date);
    
    public void addEvent(CalendarEvent ce);
    
    public List<CalendarEvent> getEvents();

}
