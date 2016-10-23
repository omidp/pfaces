package com.omidbiz.core.component;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;

@FacesComponent(value = TimeTable.COMPONENT_TYPE)
@ResourceDependencies({ @ResourceDependency(library = "pfaces", name = "components.js"),
        @ResourceDependency(library = "pfaces", name = "components.css") })
public class TimeTable extends UIInput
{

    public static final String COMPONENT_TYPE = "com.omidbiz.faces.TimeTable";
    public static final String COMPONENT_FAMILY = "com.omidbiz.faces.components";

    @Override
    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    protected enum PropertyKeys {

        style, styleClass, disabled, onselect, value;

        String toString;

        PropertyKeys(String toString)
        {
            this.toString = toString;
        }

        PropertyKeys()
        {
        }

        public String toString()
        {
            return ((this.toString != null) ? this.toString : super.toString());
        }

    }

    public boolean isDisabled()
    {
        return (Boolean) getStateHelper().eval(PropertyKeys.disabled, false);

    }

    public void setDisabled(boolean disabled)
    {
        getStateHelper().put(PropertyKeys.disabled, disabled);
    }
    
   

    public String getStyle()
    {
        return (String) getStateHelper().eval(PropertyKeys.style);

    }

    /**
     * <p>
     * Set the value of the <code>style</code> property.
     * </p>
     */
    public void setStyle(String style)
    {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public String getStyleClass()
    {
        return (String) getStateHelper().eval(PropertyKeys.styleClass);

    }

    /**
     * <p>
     * Set the value of the <code>styleClass</code> property.
     * </p>
     */
    public void setStyleClass(String styleClass)
    {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }
    
    
    public String getOnselect()
    {
        return (String) getStateHelper().eval(PropertyKeys.onselect);

    }

   
    public void setOnselect(String onselect)
    {
        getStateHelper().put(PropertyKeys.onselect, onselect);
    }
    
    
}
