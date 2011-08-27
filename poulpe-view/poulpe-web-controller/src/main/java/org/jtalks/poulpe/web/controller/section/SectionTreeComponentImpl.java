package org.jtalks.poulpe.web.controller.section;

import java.util.Iterator;
import java.util.List;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Persistent;
import org.jtalks.poulpe.model.entity.Section;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zul.Button;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

public class SectionTreeComponentImpl extends Div implements IdSpace,
		SectionTreeComponent {
	private static final String ZUL_REF = "WEB-INF/pages/sectionTree.zul";
	private Tree sectionTree;
	private SectionPresenter presenter;

	private static TreeitemRenderer treeRenderer = new TreeitemRenderer() {
		@Override
		public void render(final Treeitem treeItem, Object node)
				throws Exception {

			ExtendedTreeNode curNode = (ExtendedTreeNode) node;

			final Persistent data = (Persistent) curNode.getData();
			treeItem.setOpen(curNode.isExpanded());// Whether open the node
			treeItem.setValue(data);
			Iterator iter = treeItem.getChildren().iterator();

			while (iter.hasNext()) {
				Object child = iter.next();
				if (child instanceof Treerow) {
					for (Object oCell : ((Treerow) child).getChildren()) {
						if (oCell instanceof Treecell) {
							if (data instanceof Section) {
								((Treecell) oCell).setLabel(((Section) data)
										.getName());
							} else if (data instanceof Branch) {
								((Treecell) oCell).setLabel(((Branch) data)
										.getName());
							}
						}
					}
					return;
				}
			}

			Treerow treeRow = new Treerow();
			treeItem.appendChild(treeRow);
			if (data instanceof Section) {
				treeRow.appendChild(new Treecell(((Section) data).getName()));
			} else if (data instanceof Branch) {
				treeRow.appendChild(new Treecell(((Branch) data).getName()));

			}

		}
	};

	public SectionTreeComponentImpl(Section section, SectionPresenter presenter) {
		this.presenter = presenter;
		Executions.createComponents(ZUL_REF, this, null);
		Components.wireVariables(this, this);
		Components.addForwards(this, this);

		DefaultTreeNode child = TreeNodeFactory.getTreeNode(section);
		DefaultTreeModel model = new DefaultTreeModel(new DefaultTreeNode(null,
				new DefaultTreeNode[] { child }));
		sectionTree.setModel(model);
		sectionTree.setItemRenderer(treeRenderer);

	}

	@Override
	public Object getSelectedObject() {
		if (sectionTree.getSelectedCount() != 0) {
			Object data = sectionTree.getSelectedItem().getValue();
			return data;
		}
		return null;
	}

	public void onClick$addBranchButton() {
		presenter.openNewBranchDialog(this);
	}

	public void onDoubleClick$sectionTree() {
		presenter.openEditDialog(this);
	}

	@Override
	public void addBranchToView(Branch branch) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBranchInView(Branch branch) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeBranchFromView(Branch branch) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSectionInView(Section section) {
		DefaultTreeNode root = (DefaultTreeNode) sectionTree.getModel()
				.getRoot();
		ExtendedTreeNode sectionNode = (ExtendedTreeNode) root.getChildAt(0);
		sectionNode.setData(section);
	}

	/**
	 * Event which happen when user click on '-' button after it selected
	 * section is going to be deleted
	 */
	public void onClick$delButton() {
		Object selectedObject = getSelectedObject();
		if (selectedObject == null) {
			// if the there no selected object
			// we should consider this event as a try to remove section
			selectedObject = ((ExtendedTreeNode) ((DefaultTreeNode) sectionTree
					.getModel().getRoot()).getChildAt(0)).getData();
		}

		presenter.openDeleteDialog(selectedObject);

	}
}
