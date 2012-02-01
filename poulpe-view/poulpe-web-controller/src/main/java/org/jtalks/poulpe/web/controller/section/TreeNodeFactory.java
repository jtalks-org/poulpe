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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;

/**
 * This class should be used to wrap one-to-many related persistent entities
 * into ExtendedTreeNode structures. To be able to handle entity it should be
 * described within getTreeNode(Entity) method.
 * 
 * @author Konstantin Akimov
 * 
 */
class TreeNodeFactory {

    /**
     * Wrap single entity to DefaultTreeNode. If this entity has some related
     * object in one-to-many relation them can be either be wrapped
     * 
     * @param entity section or branch instance
     * @return node
     */
    // TODO: it's strange and unclear, refactoring is needed
    public static <T extends Entity> ExtendedTreeNode<T> getTreeNode(T entity) {
        if (entity == null) {
            return null;
        }
        if (entity instanceof Section) {
            @SuppressWarnings("unchecked")
            List<T> branches = (List<T>) ((Section) entity).getBranches();
            return new ExtendedTreeNode<T>(entity, getTreeNodes(branches));
        } else if (entity instanceof Branch) {
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
