package org.jtalks.poulpe.model.logic;

import org.jtalks.poulpe.model.dao.BranchDao;
import org.jtalks.poulpe.model.dao.SectionDao;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;

import java.util.List;

/**
 * @author stanislav bashkirtsev
 */
public class ForumStructure {
    private SectionDao sectionDao;
    private BranchDao branchDao;

    public void moveBranch(PoulpeBranch branch, PoulpeSection toSection){
//        sectionDao.moveBranchToSection(branch, toSection);
    }

    public void deleteSectionWithBranches(PoulpeSection section) {
        sectionDao.delete(section);
    }
}
