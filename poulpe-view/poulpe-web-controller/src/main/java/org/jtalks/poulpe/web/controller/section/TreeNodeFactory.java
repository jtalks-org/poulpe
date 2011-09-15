package org.jtalks.poulpe.web.controller.section;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.model.entity.Branch;

import org.jtalks.poulpe.model.entity.Section;
import org.zkoss.zul.DefaultTreeNode;

/**
 * This class should be used to wrap one-to-many related persistent entities
 * into ExtendedTreeNode structures.
 * 
 * To be able to handle entity it should be described within
 * getTreeNode(Persistent) method
 * 
 * @author costa
 * 
 */
public class TreeNodeFactory {

	/**
	 * Wrap single entity to DefaultTreeNode. If this entity has some related
	 * object in one-to-many relation them can be either be wrapped
	 * 
	 * @param entity
	 * @return
	 */
	public static ExtendedTreeNode getTreeNode(Entity entity) {
		if (entity == null)
			return null;
		if (entity instanceof Section) {
			return new ExtendedTreeNode(entity,
					getTreeNodes(((Section) entity).getBranches()));
		} else if (entity instanceof Branch) {
			return new ExtendedTreeNode(entity);
		}
		return null;
	}

	/**
	 * Wrap a List of persistent entities
	 * 
	 * @param entities
	 * @return
	 */
	public static List<ExtendedTreeNode> getTreeNodes(
			List<? extends Entity> entities) {
		List<ExtendedTreeNode> list = new ArrayList<ExtendedTreeNode>();
		if (entities == null) {
			return list;
		}
		for (Entity entity : entities) {
			if (entity == null) {
				continue;
			}
			list.add(getTreeNode(entity));
		}
		return list;
	}
}
