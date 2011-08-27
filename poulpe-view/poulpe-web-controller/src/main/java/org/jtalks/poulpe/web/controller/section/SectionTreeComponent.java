package org.jtalks.poulpe.web.controller.section;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;

public interface SectionTreeComponent {

	public void addBranchToView(Branch branch);

	public void removeBranchFromView(Branch branch);

	public void updateBranchInView(Branch branch);

	public void updateSectionInView(Section section);

	public Object getSelectedObject();
}
