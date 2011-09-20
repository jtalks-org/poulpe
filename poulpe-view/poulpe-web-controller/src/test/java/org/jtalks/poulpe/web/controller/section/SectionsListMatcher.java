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
