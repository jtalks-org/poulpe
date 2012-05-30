package org.jtalks.poulpe.web.controller.section;

import org.jtalks.poulpe.web.controller.zkutils.ZkTreeModel;
import org.jtalks.poulpe.web.controller.zkutils.ZkTreeNode;
import org.zkoss.zul.TreeNode;

import javax.annotation.Nonnull;

/**
 * A tree model specifically dedicated to work with forum structure.
 *
 * @author stanislav bashkirtsev
 */
public class ForumStructureTreeModel extends ZkTreeModel<ForumStructureItem> {
    private static final long serialVersionUID = 20110138264143L;
    public ForumStructureTreeModel(@Nonnull ZkTreeNode<ForumStructureItem> root) {
        super(root);
    }

    /**
     * Puts the specified branch to the specified section. It moves the branch from its parent if it's already existing
     * one or it will simply create a new node inside the section.The branch doesn't change its position if it's already
     * in the specified section.
     *
     * @param branchToPut        a branch item to be moved/added to the specified section
     * @param destinationSection a section that is accepting a specified branch
     * @return this
     */
    public ForumStructureTreeModel putBranch(ForumStructureItem branchToPut, ForumStructureItem destinationSection) {
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
