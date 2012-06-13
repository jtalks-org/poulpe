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
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.web.controller.zkutils.ZkTreeNode;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates a tree structure to be shown at the forum structure page.
 *
 * @author stanislav bashkirtsev
 */
public class TreeNodeFactory {
    /**
     * Creates the whole tree of sections and branches without root element (root is {@code null}).
     *
     * @param jcommune the forum structure container to get sections and branches of it
     * @return the whole tree of sections and branches built
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static ZkTreeNode<ForumStructureItem> buildForumStructure(@Nonnull Jcommune jcommune) {
        List<ZkTreeNode> sectionNodes = wrapInTreeNodes(jcommune.getSections());
        return new ZkTreeNode(jcommune, sectionNodes);
    }

    /**
     * Wrap single entity to ZkTreeNode. If this entity has some related object in one-to-many relation them can be
     * either be wrapped
     *
     * @param entity section or branch instance
     * @return node
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T extends Entity> ZkTreeNode getTreeNode(T entity) {
        if (entity instanceof PoulpeSection) {
            List<T> branches = (List<T>) ((PoulpeSection) entity).getBranches();
            return new ZkTreeNode(new ForumStructureItem().setItem(entity), wrapInTreeNodes(branches));
        } else {
            return new ZkTreeNode(new ForumStructureItem().setItem(entity));
        }
    }

    @SuppressWarnings({ "rawtypes" })
    private static <T extends Entity> List<ZkTreeNode> wrapInTreeNodes(List<T> entities) {
        List<ZkTreeNode> list = new ArrayList<ZkTreeNode>();
        for (T entity : entities) {
            list.add(getTreeNode(entity));
        }
        return list;
    }
}
