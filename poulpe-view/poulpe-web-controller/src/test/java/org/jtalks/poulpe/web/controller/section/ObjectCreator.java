package org.jtalks.poulpe.web.controller.section;

import java.util.ArrayList;
import java.util.List;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;

public class ObjectCreator {
	public static List<Section> getFakeSections(int sizeOfCollection) {
		List<Section> sections = new ArrayList<Section>();
		for (int i = 0; i < sizeOfCollection; i++) {
			sections.add(getFakeSection("fake " + i, "description " + i));
		}
		return sections;
	}

	public static  Section getFakeSection(String name, String description) {
		Section section = new Section();
		section.setName(name);
		section.setDescription(description);
		return section;
	}

	public static  Branch getFakeBranch(String name, String description) {
		Branch branch = new Branch();
		branch.setName(name);
		branch.setDescription(description);
		return branch;
	}
}
