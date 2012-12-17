package org.jtalks.poulpe.web.controller.users;

import org.jtalks.common.model.entity.Group;

import java.text.Collator;
import java.util.Locale;

/**
 * @author Leonid Kazancev
 */
public class GroupBooleanPair implements Comparable<GroupBooleanPair> {
    private boolean enable;
    private boolean changed;
    private Group group;

    public GroupBooleanPair(Group group, boolean enable) {
        this.enable = enable;
        this.group = group;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
        changed = !changed;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public int compareTo(GroupBooleanPair pair) {
        String name1 = group.getName();
        String name2 = pair.getGroup().getName();

        Boolean isEnable1 = enable;
        Boolean isEnable2 = pair.isEnable();

        if (!isEnable1.equals(isEnable2)) {
            return isEnable2.compareTo(isEnable1);
        } else {
            Collator russianCollator = Collator.getInstance(new Locale("ru", "RU"));
            return russianCollator.compare(name1, name2);
        }
    }
}
