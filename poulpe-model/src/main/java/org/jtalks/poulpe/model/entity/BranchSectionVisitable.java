package org.jtalks.poulpe.model.entity;

/**
 * Interface for implementing visitor pattern for {@link PoulpeBranch} and
 * {@link PoulpeSection} classes. Mainly for using in PoulpeSection view where, depending on
 * selected element, needed actions should be performed.<br>
 * <br>
 * 
 * This interface should be implemented by {@link PoulpeBranch} and {@link PoulpeSection}
 * 
 * @author Alexey Grigorev
 * 
 */
public interface BranchSectionVisitable {
    /**
     * Applies given visitor to this object
     * 
     * @param visitor to be applied
     */
    void apply(BranchSectionVisitor visitor);
}
