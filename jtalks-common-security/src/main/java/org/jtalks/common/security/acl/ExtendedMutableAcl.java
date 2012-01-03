package org.jtalks.common.security.acl;

import org.springframework.security.acls.model.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author stanislav bashkirtsev
 */
public class ExtendedMutableAcl implements MutableAcl {
    private final MutableAcl acl;

    public ExtendedMutableAcl(MutableAcl acl) {
        this.acl = acl;
    }

    public int delete(AccessControlEntry entryToDelete) {
        List<AccessControlEntry> aclEntries = acl.getEntries();
        for (int i = 0; i < aclEntries.size(); i++) {
            if (aclEntries.get(i).equals(entryToDelete)) {
                acl.deleteAce(i);
                return i;
            }
        }
        return -1;
    }

    public static ExtendedMutableAcl castAndCreate(Acl acl) {
        return new ExtendedMutableAcl((MutableAcl) acl);
    }

    public void delete(List<AccessControlEntry> entriesToDelete) {
        for (AccessControlEntry next : entriesToDelete) {
            delete(next);
        }
    }

    public MutableAcl getAcl() {
        return acl;
    }

    public void deleteAce(int aceIndex) throws NotFoundException {
        acl.deleteAce(aceIndex);
    }

    public void insertAce(int atIndexLocation, Permission permission, Sid sid, boolean granting) throws NotFoundException {
        acl.insertAce(atIndexLocation, permission, sid, granting);
    }

    public List<AccessControlEntry> getEntries() {
        return acl.getEntries();
    }

    public Serializable getId() {
        return acl.getId();
    }

    public ObjectIdentity getObjectIdentity() {
        return acl.getObjectIdentity();
    }

    public boolean isEntriesInheriting() {
        return acl.isEntriesInheriting();
    }

    public boolean isGranted(List<Permission> permission, List<Sid> sids, boolean administrativeMode) throws NotFoundException, UnloadedSidException {
        return acl.isGranted(permission, sids, administrativeMode);
    }

    public boolean isSidLoaded(List<Sid> sids) {
        return acl.isSidLoaded(sids);
    }

    public void setEntriesInheriting(boolean entriesInheriting) {
        acl.setEntriesInheriting(entriesInheriting);
    }

    public void setOwner(Sid newOwner) {
        acl.setOwner(newOwner);
    }

    public Sid getOwner() {
        return acl.getOwner();
    }

    public void setParent(Acl newParent) {
        acl.setParent(newParent);
    }

    public Acl getParentAcl() {
        return acl.getParentAcl();
    }

    public void updateAce(int aceIndex, Permission permission) throws NotFoundException {
        acl.updateAce(aceIndex, permission);
    }

}
