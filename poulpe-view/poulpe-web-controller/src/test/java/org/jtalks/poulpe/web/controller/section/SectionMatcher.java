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
package org.jtalks.poulpe.web.controller.section;

import org.jtalks.poulpe.model.entity.Section;
import org.mockito.ArgumentMatcher;

public class SectionMatcher extends ArgumentMatcher<Section> {

    Section section;

    String name;
    String description;

    public SectionMatcher(Section section) {
        this.section = section;
    }

    public SectionMatcher(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean matches(Object argument) {
        if (!(argument instanceof Section))
            return false;
        Section arg = (Section) argument;
        if (this.section != null) {
            return arg.equals(section);
        } else {
            return arg.getName().equals(name) && arg.getDescription().equals(description);
        }
    }
}
