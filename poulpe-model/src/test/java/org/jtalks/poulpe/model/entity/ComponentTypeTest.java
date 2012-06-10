package org.jtalks.poulpe.model.entity;

import static org.testng.Assert.*;
import java.util.Collections;
import java.util.List;

import org.jtalks.common.model.entity.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ComponentTypeTest {

    @Test(dataProvider = "componentTypes")
    public void newComponent_forum(ComponentType componentType) {
        List<Property> properties = Collections.emptyList();
        Component component = componentType.newComponent("name", "description", properties);
        assertEquals(component.getComponentType(), componentType);
    }
    
    @DataProvider
    public Object[][] componentTypes() {
        return new Object[][] { { ComponentType.ADMIN_PANEL }, { ComponentType.ARTICLE }, { ComponentType.FORUM } };
    }
}
