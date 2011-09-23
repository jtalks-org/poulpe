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

import java.util.List;

import org.jtalks.poulpe.model.entity.Section;
import org.mockito.ArgumentMatcher;

public class SectionsListMatcher extends ArgumentMatcher<List<Section>> {

    List<Section> sections;

    public SectionsListMatcher(List<Section> listSections) {
        this.sections = listSections;
    }

    @Override
    public boolean matches(Object argument) {
        if (!(argument instanceof List))
            return false;
        
        List list =(List)argument;
        for(Object ob : list){
            if(!sections.contains(ob)){
                return false;
            }
        }    
        for(Section sec : sections){
            if(!list.contains(sec)){
                return false;
            }
        }        
        return true;
    }
}
