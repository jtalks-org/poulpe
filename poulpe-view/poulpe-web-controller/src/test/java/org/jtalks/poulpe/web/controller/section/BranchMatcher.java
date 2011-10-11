package org.jtalks.poulpe.web.controller.section;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;
import org.mockito.ArgumentMatcher;

public class BranchMatcher extends ArgumentMatcher<Branch>{
	Branch branch;

    public BranchMatcher(Branch branch) {
        this.branch = branch;
    }
    

    @Override
    public boolean matches(Object argument) {
        if (!(argument instanceof Branch))
            return false;
        Branch arg = (Branch) argument;
        return arg.equals(branch);
    }
}
