package org.jtalks.poulpe.model.dto.groups;

import com.google.common.collect.Maps;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.model.permissions.BranchPermission;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author stanislav bashkirtsev
 */
public class BranchAccessList {
    private final ConcurrentMap<BranchPermission, GroupAccessList> accessListMap = Maps.newConcurrentMap();

    public BranchAccessList() {
    }

    public BranchAccessList(Map<BranchPermission, GroupAccessList> addToAccessList) {
        accessListMap.putAll(addToAccessList);
    }

    public BranchAccessList put(BranchPermission permission, Group toAllow, Group toRestrict) {
        this.accessListMap.putIfAbsent(permission, new GroupAccessList());
        GroupAccessList accessList = this.accessListMap.get(permission);
        accessList.addAllowed(toAllow).addRestricted(toRestrict);
        return this;
    }

    public BranchAccessList addAllowed(BranchPermission permission, Group group) {
        return put(permission, group, null);
    }

    public BranchAccessList addRestricted(BranchPermission permission, Group group) {
        return put(permission, null, group);
    }

    public BranchAccessList put(BranchPermission permission, Group group, boolean allow) {
        return allow ? addAllowed(permission, group) : addRestricted(permission, group);
    }

    public List<Group> getAllowed(BranchPermission permission) {
        return accessListMap.get(permission).getAllowed();
    }

    public List<Group> getRestricted(BranchPermission permission) {
        return accessListMap.get(permission).getRestricted();
    }

    public Set<BranchPermission> getPermissions() {
        return accessListMap.keySet();
    }

    public static BranchAccessList create(List<BranchPermission> permissions) {
        return new BranchAccessList(withEmptyAccessList(permissions));
    }

    private static Map<BranchPermission, GroupAccessList> withEmptyAccessList(List<BranchPermission> permissions) {
        Map<BranchPermission, GroupAccessList> newAccessListMap = Maps.newHashMap();
        for (BranchPermission permission : permissions) {
            newAccessListMap.put(permission, new GroupAccessList());
        }
        return newAccessListMap;
    }
}
