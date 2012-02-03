package org.jtalks.poulpe.model.entity;

/**
 * Interface for implementing visitor pattern for {@link Branch} and
 * {@link Section} classes. Mainly for using in Section view where, depending on
 * selected element, needed actions should be performed.<br>
 * <br>
 * 
 * This interface should be implemented by {@link Branch} and {@link Section}
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
