package org.jtalks.poulpe.web.controller.users;

import org.jtalks.common.model.entity.Group;

import java.text.Collator;
import java.util.Locale;

/**
 * Wires {@link Group} and {@link Boolean} by enable selected status for group, allows to make interface with
 * list of groups wired with checkboxes.
 * @author Leonid Kazancev
 */
public class GroupBooleanPair implements Comparable<GroupBooleanPair> {
    private boolean enable;
    private boolean changed;
    private Group group;

    /**
     * Construction method.
     * @param group to hold
     * @param enable status of group
     */
    public GroupBooleanPair(Group group, boolean enable) {
        this.enable = enable;
        this.group = group;
    }

    /**
     * @return enable status
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * @param enable status to set
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
        changed = !changed;
    }

    /**
     * @return changed status(holds group color)
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * @return {@link Group}
     */
    public Group getGroup() {
        return group;
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
