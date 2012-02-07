package org.jtalks.common.security.acl;

import org.jtalks.common.model.dao.GroupDao;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.permissions.BranchPermission;
import org.springframework.security.acls.model.AccessControlEntry;

/**
 * Created by IntelliJ IDEA. User: ctapobep Date: 1/27/12 Time: 6:53 PM To change this template use File | Settings |
 * File Templates.
 */
public class GroupAce {
    private final AccessControlEntry ace;

    public GroupAce(AccessControlEntry ace) {
        this.ace = ace;
    }

    public Group getGroup(GroupDao groupDao) {
        String groupId = ((UserGroupSid) ace.getSid()).getGroupId();
        Group group = groupDao.get(Long.parseLong(groupId));
        throwIfNull(groupId, group);
        return group;
    }

    public BranchPermission getBranchPermission() {
        return BranchPermission.findByMask(getBranchPermissionMask());
    }

    public int getBranchPermissionMask() {
        return ace.getPermission().getMask();
    }

    public boolean isGranting() {
        return ace.isGranting();
    }

    private void throwIfNull(String groupId, Group group) {
        if(group == null){
            throw new ObsoleteAclException(groupId);
        }
    }

    public static class ObsoleteAclException extends RuntimeException {

        public ObsoleteAclException(String groupId) {
            super(new StringBuilder("A group with ID [").append(groupId).append("] was removed")
                    .append("but this ID is still registered as a Permission owner (SID) in ACL tables. ")
                    .append("To resolve this issue you should manually remove records from ACL tables ")
                    .append("Note, that this is a bug and this issue should be reported to be corrected in ")
                    .append("future versions.").toString());
        }
    }
}
