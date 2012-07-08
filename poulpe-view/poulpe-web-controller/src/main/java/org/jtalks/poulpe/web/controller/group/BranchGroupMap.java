package org.jtalks.poulpe.web.controller.group;

import com.google.common.collect.ImmutableList;
import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.model.entity.PoulpeBranch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Leonid Kazancev
 */
public class BranchGroupMap {
    private List<Group> allAvailableGroups;
    private List<ModeratingGroupComboboxRow> branchesCollection = new ArrayList<ModeratingGroupComboboxRow>(20);
    private Iterator<ModeratingGroupComboboxRow> iterator;

    public BranchGroupMap(List<PoulpeBranch> branches, List<Group> groups) {
        allAvailableGroups = ImmutableList.copyOf(groups);
        for (PoulpeBranch branch : branches) {
            branchesCollection.add(new ModeratingGroupComboboxRow(branch));
        }
        iterator = branchesCollection.iterator();
    }

    public void setSelectedGroupForAllBranches(Group group) {
        for (ModeratingGroupComboboxRow controller : branchesCollection) {
            controller.setSelectedGroup(group);
        }
    }

    public List<ModeratingGroupComboboxRow> getBranchesCollection() {
        return branchesCollection;
    }

    public void setModeratingGroupForAllBranches(Group selectedGroup) {
        if (iterator.hasNext()) {
            ModeratingGroupComboboxRow controller = iterator.next();
            if (controller.hasChanges(selectedGroup)) {
                while (iterator.hasNext()) {
                    controller.setModeratorsGroup();
                    iterator.remove();
                    controller = iterator.next();
                }
            }
        }
    }


    public void setModeratingGroupForCurrentBranch(Group selectedGroup, PoulpeBranch branch) {
        while (iterator.hasNext()) {
            ModeratingGroupComboboxRow controller = iterator.next();
            if (controller.currentBrunchHasChanges(selectedGroup, branch)) {
                controller.setModeratorsGroup();
                iterator.remove();
            }
        }
    }


    public List<Group> getAllAvailableGroups() {
        return allAvailableGroups;
    }

    public void setAllAvailableGroups(List<Group> allAvailableGroups) {
        this.allAvailableGroups = allAvailableGroups;
    }
}
