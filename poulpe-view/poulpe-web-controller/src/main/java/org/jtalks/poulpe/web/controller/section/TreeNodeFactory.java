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
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class should be used to wrap one-to-many related persistent entities into ExtendedTreeNode structures. To be
 * able to handle entity it should be described within getTreeNode(Entity) method.
 *
 * @author Konstantin Akimov
 */
public class TreeNodeFactory {
    /**
     * Creates the whole tree of sections and branches without root element (root is {@code null}).
     *
     * @param jcommune the forum structure container to get sections and branches of it
     * @return the whole tree of sections and branches built
     */
    @SuppressWarnings("unchecked")
    public static ExtendedTreeNode buildForumStructure(@Nonnull Jcommune jcommune) {
        List<ExtendedTreeNode<PoulpeSection>> sectionNodes = getTreeNodes(jcommune.getSections());
        return new ExtendedTreeNode(null, sectionNodes);
    }

    /**
     * Wrap single entity to DefaultTreeNode. If this entity has some related object in one-to-many relation them can be
     * either be wrapped
     *
     * @param entity section or branch instance
     * @return node
     */
    public static <T extends Entity> ExtendedTreeNode<T> getTreeNode(T entity) {
        if (entity == null) {
            return null;
        }
        if (entity instanceof PoulpeSection) {
            @SuppressWarnings("unchecked")
            List<T> branches = (List<T>) ((PoulpeSection) entity).getBranches();
            return new ExtendedTreeNode<T>(entity, getTreeNodes(branches));
        } else if (entity instanceof PoulpeBranch) {
            return new ExtendedTreeNode<T>(entity);
        }
        return null;
    }


    /**
     * Wrap a List of persistent entities
     *
     * @param entities list of entities
     * @return list of nodes
     */
    public static <T extends Entity> List<ExtendedTreeNode<T>> getTreeNodes(List<T> entities) {
        if (entities != null) {
            return wrapInTreeNodes(entities);
        } else {
            return Collections.emptyList();
        }
    }

    private static <T extends Entity> List<ExtendedTreeNode<T>> wrapInTreeNodes(List<T> entities) {
        List<ExtendedTreeNode<T>> list = new ArrayList<ExtendedTreeNode<T>>();

        for (T entity : entities) {
            // TODO: can it be null?
            if (entity != null) {
                list.add(getTreeNode(entity));
            }
        }

        return list;
    }
}
