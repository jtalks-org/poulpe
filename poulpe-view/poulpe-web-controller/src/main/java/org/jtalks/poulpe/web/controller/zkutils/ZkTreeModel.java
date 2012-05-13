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
package org.jtalks.poulpe.web.controller.zkutils;

import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.TreeNode;

import javax.annotation.Nonnull;

/**
 * Since default ZK implementation of {@link org.zkoss.zul.TreeModel} doesn't provide handy methods e.g. for searching
 * nodes, this one is created to provide it and a lot more.
 *
 * @author stanislav bashkirtsev
 * @since 0.15
 */
public class ZkTreeModel<E> extends DefaultTreeModel<E> {
    private static final long serialVersionUID = 20110131094143L;

    /**
     * Creates a tree with the specified note as the root.
     *
     * @param root the root (cannot be null)
     */
    public ZkTreeModel(@Nonnull TreeNode<E> root) {
        super(root);
    }

    /**
     * Recursively searches for the first node that contains {@code data} as its content and returns it.
     *
     * @param data the data to find a node with
     * @return the first node with the data set to specified one
     * @see org.zkoss.zul.TreeNode#getData()
     */
    public TreeNode<E> find(@Nonnull E data) {
        return find(getRoot(), data);
    }

    /**
     * Returns a child of the tree that sits at the specified path. It's a handy analog of {@link
     * org.zkoss.zul.TreeModel#getChild(int[])}.
     *
     * @param path a list of indices of the nodes in the tree. E.g. if you specify 5,4,1, then will be taken the node
     *             with index 5 from the first level of the tree, next will be the 4th child, and then the 1st child of
     *             the 4th child.
     * @return the node at the specified path or {@code null} if there is no such path exists, or returns root element
     *         if zero size array was specified
     */
    public TreeNode<E> getChild(int... path) {
        return super.getChild(path);
    }

    private TreeNode<E> find(TreeNode<E> toSearchIn, E data) {
        if (toSearchIn.getData().equals(data)) {
            return toSearchIn;
        }
        if (toSearchIn.isLeaf()) {
            return null;
        }
        return findOnlyInChildren(toSearchIn, data);
    }

    private TreeNode<E> findOnlyInChildren(TreeNode<E> toSearchIn, E data) {
        for (TreeNode<E> nextChild : toSearchIn.getChildren()) {
            if (nextChild.getData().equals(data)) {
                return nextChild;
            }
            TreeNode<E> nextChildSearchResult = find(nextChild, data);
            if (nextChildSearchResult != null) {
                return nextChildSearchResult;
            }
        }
        return null;
    }
}
