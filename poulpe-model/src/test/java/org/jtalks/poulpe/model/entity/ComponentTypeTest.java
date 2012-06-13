/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.poulpe.model.entity;

import static org.testng.Assert.*;
import java.util.Collections;
import java.util.List;

import org.jtalks.common.model.entity.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Alexey Grigorev
 */
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
