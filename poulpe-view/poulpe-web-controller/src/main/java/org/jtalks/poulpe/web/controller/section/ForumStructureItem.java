/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.poulpe.web.controller.section;

import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The class is dedicated to contain any entity (in our particular case is either Section or Branch) so that we can
 * store the selected items of the tree into a single object (whilst without this wrapper we would need two separate
 * fields: selectedSection & selectedBranch). Also contains different useful methods like {@link #isBranch()} or {@link
 * #isPersisted()}.
 *
 * @author stanislav bashkirtsev
 */
public class ForumStructureItem {
    private final Entity item;

    /** Initialize object without wrapped item ({@code null} value used) */
    public ForumStructureItem() {
        this(null);
    }

    /**
     * @param item the entity inside of the item, can be either branch or section. Also can be {@code null} which
     *             represents a null item, but in the same time can provide useful methods without throwing NPE. See
     *             Null Object design pattern for more information.
     */
    public ForumStructureItem(@Nullable Entity item) {
        this.item = item;
    }

    /**
     * Gets the wrapped item inside. May be {@code null} if null was set via constructor.
     *
     * @return the wrapped item inside
     */
    public Entity getItem() {
        return item;
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
     * Helps to understand whether the item inside is {@link PoulpeSection} or not. Will return {@code false} in both
     * options - whether the item inside is not an instance of section or it's {@code null}.
     *
     * @return {@code true} if the item inside is an instance of section, {@code false} if the wrapped item is {@code
     *         null} or not an instance of section (e.g. a branch)
     */
    public boolean isSection() {
        return item != null && item instanceof PoulpeSection;
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
     * Returns item cast to branch, note that it throws an exception if it's not actually a branch, so use {@link
     * #isBranch()} before you use this method.
     *
     * @return a branch inside the item if it is a branch or {@code null}
     * @throws ClassCastException if the entity inside is not actually a branch
     */
    public PoulpeBranch getBranchItem() {
        return (PoulpeBranch) item;
    }


    /**
     * Returns item cast to section, note that it throws an exception if it's not actually a section, so use {@link
     * #isBranch()} before you use this method.
     *
     * @return a section inside the item if it is a section or {@code null}
     * @throws ClassCastException if the entity inside is not actually a section
     */
    public PoulpeSection getSectionItem() {
        return (PoulpeSection) item;
    }

    /**
     * Prepared the item to be edited (shown in the branch editing dialog) by returning the new instance of item or
     * returning {@code this} by depending on the specified flag.
     *
     * @param createNew specify {@code false} if you want the branch to be edited, or {@code false} if you want to
     *                  create a new branch item
     * @return new branch item if {@code createNew} was set or returns {@code this} (which will mean that the branch is
     *         rather being edited than a dialog to create a new branch was created)
     */
    public ForumStructureItem prepareBranchItemForEditing(boolean createNew) {
        return createNew ? new ForumStructureItem(new PoulpeBranch()) : this;
    }

    /**
     * Prepared the item to be edited (shown in the section editing dialog) by returning the new instance of item or
     * returning {@code this} by depending on the specified flag.
     *
     * @param createNew specify {@code false} if you want the section to be edited, or {@code false} if you want to
     *                  create a new section item
     * @return new section item if {@code createNew} was set or returns {@code this} (which will mean that the section
     *         is rather being edited than a dialog to create a new section was created)
     */
    public ForumStructureItem prepareSectionItemForEditing(boolean createNew) {
        return createNew ? new ForumStructureItem(new PoulpeSection()) : this;
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
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return item == null ? "null" : item.toString();
    }

    /**
     * Gets the list of draggable identifiers for the current item
     *
     * @return comma separated list of draggable identifiers
     */
    public String getDraggableId() {
        if (isBranch()) {
            return "branch";
        } else if (isSection()) {
            return "section";
        } else {
            return "false";
        }
    }

    /**
     * Gets the list of droppable identifiers for the current item
     *
     * @return comma separated list of droppable identifiers
     */
    public String getDroppableId() {
        if (isBranch()) {
            return "branch";
        } else if (isSection()) {
            return "branch, section";
        } else {
            return "false";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ForumStructureItem that = (ForumStructureItem) o;

        if (item != null ? !item.equals(that.item) : that.item != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return item != null ? item.hashCode() : 0;
    }
}
