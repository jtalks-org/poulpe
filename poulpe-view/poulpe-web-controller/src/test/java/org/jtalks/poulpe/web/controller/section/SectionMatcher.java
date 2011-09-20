package org.jtalks.poulpe.web.controller.section;

import org.jtalks.poulpe.model.entity.Section;
import org.mockito.ArgumentMatcher;

public class SectionMatcher extends ArgumentMatcher<Section> {

    String name;
    String description;

    public SectionMatcher(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean matches(Object argument) {
        if (!(argument instanceof Section))
            return false;
        Section arg = (Section) argument;
        if (!arg.getName().equals(name)
                || !arg.getDescription().equals(description)) {
            return false;
        }
        return true;
    }
}
