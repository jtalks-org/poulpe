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

import java.util.List;

import org.zkoss.zul.DefaultTreeNode;

/**
 * @author Konstantin Akimov
 * */
public class ExtendedTreeNode<T> extends DefaultTreeNode<T> {

    /**
     * 
     */
    private static final long serialVersionUID = -3198582085335476694L;

    /**
     * this flag is used to determine if the node is going to be expanded by
     * defaults
     */
    private boolean isExpanded = true;

    /**
     * @param entity node data
     * @param children list of childrens for node
     * @see DefaultTreeNode
     */
    public ExtendedTreeNode(T entity, ExtendedTreeNode<T>[] children) {
        super(entity, children);
    }

    /**
     * @param entity node data
     * @param children list of childrens for node
     * @see DefaultTreeNode
     */
    public ExtendedTreeNode(T entity, List<ExtendedTreeNode<T>> children) {
        super(entity, children);
    }

    /**
     * @param entity data for leaf node
     * @see DefaultTreeNode
     */
    public ExtendedTreeNode(T entity) {
        super(entity);
    }

    /**
     * @return if node expanded return true
     */
    public boolean isExpanded() {
        return isExpanded;
    }

    /**
     * set expanded status
     * 
     * @param flag true if node expanded
     */
    public void setExpanded(boolean flag) {
        this.isExpanded = flag;
    }
}
