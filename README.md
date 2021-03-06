# Persian Faces (JSF 2 Persian components)

## Clone project

```
git clone https://github.com/omidp/pfaces.git
```

## Build 

```
mvn clean install -DskipTests=true -Pjsf
```

## Build for primefaces 6.x

```
mvn clean install -DskipTests=true -Pprimefaces
```

## Add Servlet resource

* Add this in your web.xml

```
<servlet>
		<servlet-name>pfaces-resource-serlvet</servlet-name>
		<servlet-class>com.omidbiz.core.resources.ServletResource</servlet-class>		
	</servlet>
	<servlet-mapping>
		<servlet-name>pfaces-resource-serlvet</servlet-name>
		<url-pattern>/pfaces/*</url-pattern>
	</servlet-mapping>
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

## How to use

* add this into html head tag

```
<h:outputScript library="primefaces" name="jquery/jquery.js" target="head" />
<h:outputScript library="primefaces" name="jquery/jquery-plugins.js"	target="head" />
<script type="text/javascript"	src="#{request.contextPath}/pfaces/pfaces.js" />
<link type="text/css" rel="stylesheet" href="#{request.contextPath}/pfaces/pfaces.css" />
```

## Components

###### date picker



```
<pfaces:datePicker value="#{vacationHome.instance.fromDate}" showTime="true"></pfaces:datePicker>
```

Or without datetimepicker

```
<p:calendar value="#{vacationHome.instance.fromDate}" locale="fa"></pfaces:datePicker>
```


###### timetable 

![alt tag](http://cdn.persiangig.com/preview/IEodTMXT6Q/timeTable.png)


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

NOTE: If you are going to use datetime picker, you need to use [this primefaces patch](https://github.com/omidp/primefaces/tree/p6.1) 
