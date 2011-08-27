package org.jtalks.poulpe.web.controller.section;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.service.SectionService;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SectionPresenterTest extends SectionPresenter {

	private SectionPresenter presenter;

	@BeforeTest
	public void setUp() {
		presenter = new SectionPresenter();
	}

	@Test
	public void validateSectionTest() {
		SectionService service = mock(SectionService.class);
		when(service.isSectionExists(anyString())).thenReturn(true).thenReturn(
				false);
		presenter.setSectionService(service);
		String testName = "Test name";
		String testDescription = "Test name";
		assertNotNull(presenter.validateSection("", testDescription));
		assertNotNull(presenter.validateSection(testName, testDescription));
		assertNull(presenter.validateSection(testName, testDescription));
	}

	@Test
	public void initViewTest() {
		SectionService service = mock(SectionService.class);
		presenter.setSectionService(service);
		when(service.getAll()).thenReturn(getFakeSections(3));

		SectionView view = mock(SectionView.class);
		presenter.initView(view);
		

	}

	private List<Section> getFakeSections(int sizeOfCollection) {
		List<Section> sections = new ArrayList<Section>();
		for (int i = 0; i < sizeOfCollection; i++) {
			sections.add(getFakeSection("fake " + i, "description " + i));
		}
		return sections;
	}

	private Section getFakeSection(String name, String description) {
		Section section = new Section();
		section.setName(name);
		section.setDescription(description);
		return section;
	}

}
