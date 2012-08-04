package org.jtalks.poulpe.web.controller.group;

import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.model.entity.PoulpeBranch;

import java.util.List;

/**
 * Wires {@link Group} and {@link PoulpeBranch} by holding selected group for branch, allows to make interface with
 * dynamic comboboxes count.
 *
 * @author Leonid Kazancev
 */
public class ModeratingGroupComboboxRow {
    private PoulpeBranch currentBranch;
    private Group selectedGroup;

    /**
     * {@inheritDoc}
     */
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
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode(){
        return (int)currentBranch.getId();
    }

    /**
     * Construct ModeratingGroupComboboxRow by setting up {@link PoulpeBranch} 'currentBranch'. Field 'selectedGroup'
     * are not seted up, it will initialized by setter method, called from ZK.
     * @param currentBranch {@link PoulpeBranch} at currently processing row
     */
    public ModeratingGroupComboboxRow(PoulpeBranch currentBranch) {
        this.currentBranch = currentBranch;
    }

    /**
     * @return {@link Group} witch are selected for 'currentBranch'
     */
    public Group getSelectedGroup() {
        return selectedGroup;
    }

    /**
     * @param selectedGroup {@link Group} to hold as pair for 'currentBranch'
     */
    public void setSelectedGroup(Group selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    /**
     * @return {@link PoulpeBranch}
     */
    public PoulpeBranch getCurrentBranch(){
        return currentBranch;
    }

    /**
     * Checking for changes of moderator group for branch, also verifies correctnes of this compar
     * @param group
     * @param branch
     * @return
     */
    public boolean currentBrunchHasChanges(Group group, PoulpeBranch branch){
        return hasChanges(group) && currentBranch.equals(branch);
    }

    /**
     * Compares {@link Group} taken as arg with {@link Group} currently selected as moderator for current branch
     * @param group {@link Group} to compare
     * @return true if moderators group are not same, and should be changed, false otherwise
     */
    public boolean hasChanges(Group group){
        return !group.equals(selectedGroup);
    }

    /**
     * Sets moderators {@link Group} for 'currentBranch'
     */
    public void setModeratorsGroup(){
        currentBranch.setModeratorsGroup(selectedGroup);
    }
}
