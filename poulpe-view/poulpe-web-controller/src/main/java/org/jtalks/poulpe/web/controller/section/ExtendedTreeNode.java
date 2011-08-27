package org.jtalks.poulpe.web.controller.section;

import java.util.List;

import org.jtalks.poulpe.model.entity.Persistent;
import org.jtalks.poulpe.model.entity.Section;
import org.zkoss.zul.DefaultTreeNode;

public class ExtendedTreeNode extends DefaultTreeNode {

	/**
	 * this flag is used to determine if the node is going to be expanded by
	 * defaults
	 */
	private boolean isExpanded = true;

	public ExtendedTreeNode(Persistent entity, ExtendedTreeNode[] children) {
		super(entity, children);
	}

	public ExtendedTreeNode(Persistent entity, List<ExtendedTreeNode> children) {
		super(entity, children);
	}

	public ExtendedTreeNode(Persistent entity) {
		super(entity);
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean flag) {
		this.isExpanded = flag;
	}
}
