package org.jtalks.poulpe.web.controller.section;

import org.jtalks.poulpe.web.controller.zkutils.ZkTreeModel;
import org.jtalks.poulpe.web.controller.zkutils.ZkTreeNode;
import org.zkoss.zul.TreeNode;

import javax.annotation.Nonnull;

/**
 * @author stanislav bashkirtsev
 */
public class ForumStructureTreeModel extends ZkTreeModel<ForumStructureItem> {
    public ForumStructureTreeModel(@Nonnull ZkTreeNode<ForumStructureItem> root) {
        super(root);
    }

    public ForumStructureTreeModel putBranch(ForumStructureItem branchToPut, ForumStructureItem destinationSection){
        TreeNode<ForumStructureItem> destinationSectionNode = find(destinationSection);
        ZkTreeNode<ForumStructureItem> branchNodeToPut = (ZkTreeNode<ForumStructureItem>) find(branchToPut);
        if (branchNodeToPut == null) {
            branchNodeToPut = new ZkTreeNode<ForumStructureItem>(branchToPut);
        }
        branchNodeToPut.moveTo(destinationSectionNode);
        addToSelection(branchNodeToPut);
        addOpenObject(destinationSectionNode);
        return this;
    }
}
