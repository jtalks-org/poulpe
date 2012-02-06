package org.jtalks.poulpe.model.entity;

/**
 * Interface for implementing visitor pattern for {@link PoulpeBranch} and
 * {@link PoulpeSection} classes. Mainly for using in PoulpeSection view where, depending on
 * selected element, needed actions should be performed.<br>
 * <br>
 * 
 * This interface should be implemented by all visitors, applicable to
 * {@link PoulpeBranch} and {@link PoulpeSection}
 * 
 * @author Alexey Grigorev
 * 
 */
public interface BranchSectionVisitor {

    /**
     * Performs an action when the passed element is {@link PoulpeSection}
     * 
     * @param section to be processed
     */
    void visitSection(PoulpeSection section);

    /**
     * Performs an action when the passed element is {@link PoulpeBranch}
     * 
     * @param branch to be processed
     */
    void visitBranch(PoulpeBranch branch);

}
