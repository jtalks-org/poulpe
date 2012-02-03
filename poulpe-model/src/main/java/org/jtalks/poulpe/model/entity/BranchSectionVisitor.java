package org.jtalks.poulpe.model.entity;

/**
 * Interface for implementing visitor pattern for {@link Branch} and
 * {@link Section} classes. Mainly for using in Section view where, depending on
 * selected element, needed actions should be performed.<br>
 * <br>
 * 
 * This interface should be implemented by all visitors, applicable to
 * {@link Branch} and {@link Section}
 * 
 * @author Alexey Grigorev
 * 
 */
public interface BranchSectionVisitor {

    /**
     * Performs an action when the passed element is {@link Section}
     * 
     * @param section to be processed
     */
    void visitSection(Section section);

    /**
     * Performs an action when the passed element is {@link Branch}
     * 
     * @param branch to be processed
     */
    void visitBranch(Branch branch);

}
