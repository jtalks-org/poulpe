package org.jtalks.poulpe.service;

import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;

/**
 * A coarse-grained service to work with forum structure (sections, branches).
 *
 * @author stanislav bashkirtsev
 * @author Guram Savinov
 */
public interface ForumStructureService {
    void saveJcommune(Jcommune jcommune);

    Jcommune getJcommune(long id);

    void removeBranch(PoulpeBranch branch);

    void moveBranch(PoulpeBranch branch, PoulpeSection toSection);

    void moveBranch(PoulpeBranch branch, PoulpeBranch target);

    Jcommune deleteSectionWithBranches(PoulpeSection section);

    void deleteSectionAndMoveBranches(PoulpeSection toRemove, PoulpeSection toReceiveBranches);

    PoulpeBranch saveBranch(PoulpeSection inSection, PoulpeBranch notYetSavedBranch);

    void deleteBranch(PoulpeBranch branch);
}
