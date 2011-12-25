package org.jtalks.poulpe.service.mock;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;
import org.jtalks.poulpe.service.security.BranchPermission;
import org.jtalks.poulpe.service.security.JtalksPermission;

import java.util.Collection;
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
    public Table<JtalksPermission, Group, Boolean> getGroupAccessListFor(Branch branch) {
        Table<JtalksPermission, Group, Boolean> table = HashBasedTable.create();
        table.put(BranchPermission.CREATE_TOPICS, new Group("Moderator"), true);
        table.put(BranchPermission.CREATE_TOPICS, new Group("Admins"), true);
        table.put(BranchPermission.CREATE_TOPICS, new Group("Activated Users"), true);
        table.put(BranchPermission.CREATE_TOPICS, new Group("Banned Users"), false);
        table.put(BranchPermission.CREATE_TOPICS, new Group("Naughty Users"), false);
        table.put(BranchPermission.CREATE_TOPICS, new Group("Trolls"), false);
        return table;
    }

    @Override
    public Branch get(Long id) throws NotFoundException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isExist(long id) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void grantPermissions(Branch branch, JtalksPermission permission, Collection<Group> groups) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void restrictPermissions(Branch branch, JtalksPermission permission, Collection<Group> groups) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deletePermissions(Branch branch, JtalksPermission permission, Collection<Group> groups) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
