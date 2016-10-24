# Persian Faces (JSF 2 Persian components)

## Clone project

```
git clone https://github.com/omidp/pfaces.git
```

## Build 

```
mvn clean install
```

## Add dependency to your project

```
<dependency>
			<groupId>com.omidbiz</groupId>
			<artifactId>pfaces</artifactId>
			<version>0.0.1</version>
</dependency>
```

## Add namespace to your xhtml

```
xmlns:pfaces="http://omidbiz.com/ui"
```

## Components

###### date picker

```
<pfaces:datePicker value="#{vacationHome.instance.fromDate}" showTime="true"></pfaces:datePicker>
```

###### timetable 

![alt tag](https://github.com/omidp/pfaces/blob/master/timeTable.png)

```
<pfaces:timeTable value="#{calendarEventHome.model}"></pfaces:timeTable>
```

```
@Name("calendarEventHome")
public class CalendarEventHome
{

    DefaultCalendarModel model;

    @Create
    public void init()
    {
        List<CalendarEvent> events = new ArrayList<CalendarEvent>(0);
        events.add(new DefaultCalendarEvent(UUID.randomUUID().toString(), "title 1", "data", new Date(), new Date()));
        model = new DefaultCalendarModel(events);
    }

    public DefaultCalendarModel getModel()
    {
        return model;
    }

}
```


## Dependencies

Project includes

+ jQuery 1.7
+ jQuery ui 1.8.16
