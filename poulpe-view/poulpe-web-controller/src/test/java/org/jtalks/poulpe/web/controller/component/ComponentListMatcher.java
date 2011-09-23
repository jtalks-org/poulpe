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
package org.jtalks.poulpe.web.controller.component;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.jtalks.poulpe.model.entity.Component;
import org.mockito.ArgumentMatcher;

/**
 * The class for matching the lists of {@link Component} items.
 * @author Dmitriy Sukharev
 */
class ComponentListMatcher extends ArgumentMatcher<List<Component>> {
    private List<Component> list;

    public ComponentListMatcher(List<Component> list) {
        this.list = list;
    }

    @Override
    public boolean matches(Object item) {
        List<?> list2 = (List<?>) item;
        assertEquals(list.size(), list2.size());
        boolean isMatch = true;
        for (int i = 0; i < list.size(); i++) {
            Component comp = list.get(i);
            Component comp2 = (Component) list2.get(i);
            isMatch &= comp.getId() == comp2.getId() && comp.getName().equals(comp2.getName())
                    && comp.getDescription().equals(comp2.getDescription())
                    && comp.getComponentType().equals(comp2.getComponentType());
        }
        return isMatch;
    }

}