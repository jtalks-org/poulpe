package org.jtalks.poulpe.web.controller.section;

import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.model.entity.PoulpeBranch;

import javax.annotation.Nonnull;

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

    public ForumStructureItem() {
    }

    public ForumStructureItem(Entity item) {
        this.item = item;
    }

    /**
     * Gets the wrapped item inside. May be {@code null} if nothing was set through {@link #setItem(Entity)}.
     *
     * @return the wrapped item inside
     */
    public Entity getItem() {
        return item;
    }

    /**
     * Sets the item that will reside inside util another items is set or {@link #clearState()} wasn't triggered.
     *
     * @param item the item to be wrapped
     * @return this
     */
    public ForumStructureItem setItem(Entity item) {
        this.item = item;
        return this;
    }

    /**
     * Helps to understand whether the item inside is {@link PoulpeBranch} or not. Will return {@code false} in both
     * options - whether the item inside is not an instance of branch or it's {@code null}.
     *
     * @return {@code true} if the item inside is an instance of branch, {@code false} if the wrapped item is {@code
     *         null} or not an instance of branch (e.g. a section)
     */
    public boolean isBranch() {
        return item != null && item instanceof PoulpeBranch;
    }

    /**
     * Defines whether the item inside is already persisted or it's a new entity which wasn't saved yet.
     *
     * @return {@code true} if the item inside is already saved into database, {@code false} if the item is {@code null}
     *         or it wasn't saved yet
     */
    public boolean isPersisted() {
        return item != null && item.getId() != 0;
    }

    /**
     * Gets the wrapped entity and casts it to the class specified as a parameter.
     *
     * @param itemClass the class of the item inside to be casted to
     * @param <T>       the class of the item inside to be casted to
     * @return the entity of the specified class (that's wrapped) or {@code null} if the item inside is {@code null}
     * @throws ClassCastException if the class specified in the constructor is not actually a class of the wrapped
     *                            entity
     */
    public <T extends Entity> T getItem(@Nonnull Class<T> itemClass) {
        if (item == null) {
            return null;
        }
        return itemClass.cast(item);
    }

    /**
     * Sets the wrapped item to {@code null}.
     *
     * @return this
     */
    public ForumStructureItem clearState() {
        item = null;
        return this;
    }

    @Override
    public String toString() {
        return item == null ? "null" : item.toString();
    }
}
