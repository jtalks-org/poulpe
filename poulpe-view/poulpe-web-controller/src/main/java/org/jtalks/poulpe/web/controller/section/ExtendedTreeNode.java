package org.jtalks.poulpe.web.controller.section;

import java.util.List;

import org.jtalks.common.model.entity.Entity;
import org.zkoss.zul.DefaultTreeNode;

public class ExtendedTreeNode extends DefaultTreeNode {

	/**
	 * this flag is used to determine if the node is going to be expanded by
	 * defaults
	 */
	private boolean isExpanded = true;

	public ExtendedTreeNode(Entity entity, ExtendedTreeNode[] children) {
		super(entity, children);
	}

	public ExtendedTreeNode(Entity entity, List<ExtendedTreeNode> children) {
		super(entity, children);
	}

	public ExtendedTreeNode(Entity entity) {
		super(entity);
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean flag) {
		this.isExpanded = flag;
	}
}
