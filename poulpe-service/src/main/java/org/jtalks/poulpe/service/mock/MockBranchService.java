package org.jtalks.poulpe.service.mock;

import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.dto.groups.GroupAccessList;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;

import java.util.List;

/**
 * Created by IntelliJ IDEA. User: ctapobep Date: 12/21/11 Time: 5:14 PM To change this template use File | Settings |
 * File Templates.
 */
public class MockBranchService implements BranchService {
    @Override
    public List<Branch> getAll() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteBranch(Branch selectedBranch) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveBranch(Branch selectedBranch) throws NotUniqueException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteBranchRecursively(Branch victim) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteBranchMovingTopics(Branch victim, Branch recipient) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isDuplicated(Branch branch) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public GroupAccessList getGroupAccessListFor(Branch branch) {
        GroupAccessList accessList = new GroupAccessList();
        accessList.setAllowed(Group.createGroupsWithNames("Moderators", "Admins", "Activated Users"));
        accessList.setRestricted(Group.createGroupsWithNames("Banned Users", "Naughty Users", "Trolls"));
        return accessList;
    }

    @Override
    public Branch get(Long id) throws NotFoundException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isExist(long id) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
