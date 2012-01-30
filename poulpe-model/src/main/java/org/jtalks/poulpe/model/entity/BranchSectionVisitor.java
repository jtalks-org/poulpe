package org.jtalks.poulpe.model.entity;

public interface BranchSectionVisitor {

    void visitSection(Section section);

    void visitBranch(Branch branch);

}
