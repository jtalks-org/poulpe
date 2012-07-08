package org.jtalks.poulpe.web.controller.group;

import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.model.entity.PoulpeBranch;

import java.util.List;

/**
 * @author Leonid Kazancev
 */
public class ModeratingGroupComboboxRow {
    private PoulpeBranch currentBranch;
    private Group selectedGroup;

    @Override
    public boolean equals(Object object){
        if (object instanceof  PoulpeBranch)
        {PoulpeBranch otherBranch = (PoulpeBranch) object;
        return currentBranch.getId()==otherBranch.getId();
        }
        else {
            return  super.equals(object);
        }
}
    @Override
    public int hashCode(){
        return (int)currentBranch.getId();
    }


    public ModeratingGroupComboboxRow(PoulpeBranch currentBranch) {
        this.currentBranch = currentBranch;
    }

    public Group getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(Group selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public PoulpeBranch getCurrentBranch(){
        return currentBranch;
    }
    
    public boolean currentBrunchHasChanges(Group group, PoulpeBranch branch){
        return !hasChanges(group) && currentBranch.equals(branch);
    }

    public boolean hasChanges(Group group){
        return !group.equals(selectedGroup);
    }



    
    public void setModeratorsGroup(){
        currentBranch.setModeratorsGroup(selectedGroup);
    }
}
