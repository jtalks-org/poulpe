package org.jtalks.poulpe.web.controller.section.mvvm;

import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.model.entity.PoulpeBranch;

/**
 * The class is dedicated to contain any entity (in our particular case is either Section or Branch) so that we can
 * store the selected items of the tree into a single object (whilst without this wrapper we would need two separate
 * fields: selectedSection & selectedBranch). Also contains different useful methods like {@link #isBranch()} or {@link
 * #isPersisted()}.
 *
 * @author stanislav bashkirtsev
 */
public class ForumStructureItem {
    private Entity item;

    public Entity getItem() {
        return item;
    }

    public void setItem(Entity item) {
        this.item = item;
    }

    public boolean isBranch() {
        return item != null && item instanceof PoulpeBranch;
    }

    public boolean isPersisted() {
        return item != null && item.getId() != 0;
    }

    public <T extends Entity> T getItem(Class<T> itemClass) {
        if (item == null) {
            return null;
        }
        return itemClass.cast(item);
    }

    public void clearState() {
        item = null;
    }
}
