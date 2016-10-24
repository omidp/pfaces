# Persian Faces (JSF 2 Persian components)

+ clone project

```
git clone https://github.com/omidp/pfaces.git
```

+ build 

```
mvn clean install
```

+ add dependency to your project

```
<dependency>
			<groupId>com.omidbiz</groupId>
			<artifactId>pfaces</artifactId>
			<version>0.0.1</version>
</dependency>
```

+ add namespace 

```
xmlns:pfaces="http://omidbiz.com/ui"
```

+ use components

+ date picker component

```
<pfaces:datePicker value="#{vacationHome.instance.fromDate}" showTime="true"></pfaces:datePicker>
```

+ timetable component

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


+ dependencies

this project depends on 

+ jQuery 1.7
+ jQuery ui 1.8.16
