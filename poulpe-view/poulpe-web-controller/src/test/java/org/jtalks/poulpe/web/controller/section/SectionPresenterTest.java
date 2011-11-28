/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.poulpe.web.controller.section;

import static org.jtalks.poulpe.web.controller.utils.ObjectCreator.getFakeBranch;
import static org.jtalks.poulpe.web.controller.utils.ObjectCreator.getFakeSection;
import static org.jtalks.poulpe.web.controller.utils.ObjectCreator.getFakeSections;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.model.entity.TopicType;
import org.jtalks.poulpe.service.SectionService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.utils.BranchMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * 
 * @author costa
 * @author Vahluev Vyacheslav
 * 
 */
public class SectionPresenterTest extends SectionPresenter {

	private SectionPresenter presenter;

	@Mock
	private SectionService service;

	@Mock
	SectionView view;
	@Mock
	DialogManager dialogManager;

	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		presenter = new SectionPresenter();
		presenter.setSectionService(service);
		presenter.setDialogManager(dialogManager);
	}

	@Test
	public void testValidateSection() {

		String testName = "Test name";
		String testDescription = "Test name";
		assertEquals(presenter.validateSection("", testDescription),
				SectionPresenter.ERROR_LABEL_SECTION_NAME_CANT_BE_VOID);
		assertNotNull(presenter.validateSection(null, testDescription),
				SectionPresenter.ERROR_LABEL_SECTION_NAME_CANT_BE_VOID);
		assertNull(presenter.validateSection(testName, testDescription));
	}

	@Test
	public void testCheckSectionUniqueness() {
		Section section = getFakeSection(anyString(), "");
		when(service.isSectionExists(section)).thenReturn(true).thenReturn(
				false);

		String testName = "Test name";
		String testDescription = "Test name";
		assertEquals(presenter.checkSectionUniqueness(null, testDescription),
				SectionPresenter.ERROR_LABEL_SECTION_NAME_CANT_BE_VOID);
		assertEquals(presenter.checkSectionUniqueness("", testDescription),
				SectionPresenter.ERROR_LABEL_SECTION_NAME_CANT_BE_VOID);
		assertEquals(
				presenter.checkSectionUniqueness(testName, testDescription),
				SectionPresenter.ERROR_LABEL_SECTION_NAME_ALREADY_EXISTS);
		assertNull(presenter.checkSectionUniqueness(testName, testDescription));
	}

	@Test
	public void testInitView() {

		List<Section> fakeSections = getFakeSections(3);
		when(service.getAll()).thenReturn(fakeSections);

		presenter.initView(view);

		verify(view).showSections(
				argThat(new SectionsListMatcher(fakeSections)));
	 	verify(view).closeDialogs();
	}

	@Test
	public void testOpenDeleteDialog() {

		List<Section> fakeSections = getFakeSections(9);
		List<Section> cloneOfFakeSections = new ArrayList<Section>();
		for (int i = 0; i < fakeSections.size(); i++) {
			if (i != 3) {
				cloneOfFakeSections.add(fakeSections.get(i));
			}
		}

		when(service.getAll()).thenReturn(fakeSections);
		presenter.initView(view);

		presenter.openDeleteDialog(fakeSections.get(3));
		verify(view).openDeleteSectionDialog(
				argThat(new SectionMatcher(fakeSections.get(3))));

	}

	@Test
	public void testOpenDeleteDialogWithWrongArguments() {

		List<Section> fakeSections = getFakeSections(9);
		List<Section> cloneOfFakeSections = new ArrayList<Section>();
		for (int i = 0; i < fakeSections.size(); i++) {
			if (i != 3) {
				cloneOfFakeSections.add(fakeSections.get(i));
			}
		}

		// verify that only not null object can be deleted
		presenter.openDeleteDialog(null);
		verify(view, never()).openDeleteSectionDialog(
				argThat(new SectionMatcher(fakeSections.get(3))));

		// verify that only Section or Branch can be deleted
		presenter.openDeleteDialog(new TopicType());
		verify(view, never()).openDeleteSectionDialog(
				argThat(new SectionMatcher(fakeSections.get(3))));

		Branch fakeBranch = getFakeBranch("test branch", "test branch");
		presenter.openDeleteDialog(fakeBranch);
		verify(dialogManager).confirmDeletion(anyString(),
				argThat(new PerformableMatcher(new DialogManager.Performable() {
					@Override
					public void execute() {
					}
				})));
	}

	@Test
	public void testOpenEditDialog() {
		List<Section> fakeSections = getFakeSections(9);
		Branch fakeBranch = getFakeBranch("test", "test");
		when(service.getAll()).thenReturn(fakeSections);
		presenter.initView(view);

		SectionTreeComponent sectionTreeComponent = mock(SectionTreeComponent.class);
		when(sectionTreeComponent.getSelectedObject()).thenReturn(null)
				.thenReturn(new TopicType()).thenReturn(fakeSections.get(3))
				.thenReturn(fakeBranch);

		// null
		presenter.openEditDialog(sectionTreeComponent);
		verify(view, never()).openEditBranchDialog(
				argThat(new BranchMatcher(new Branch())));
		verify(view, never()).openEditSectionDialog(anyString(), anyString());

		// TopicType object
		presenter.openEditDialog(sectionTreeComponent);
		verify(view, never()).openEditBranchDialog(
				argThat(new BranchMatcher(new Branch())));
		verify(view, never()).openEditSectionDialog(anyString(), anyString());

		// section
		presenter.openEditDialog(sectionTreeComponent);
		verify(view, times(1)).openEditSectionDialog(
				fakeSections.get(3).getName(),
				fakeSections.get(3).getDescription());
		assertEquals(presenter.getCurrentSectionTreeComponent(),
				sectionTreeComponent);
		presenter.setCurrentSectionTreeComponent(null);

		// branch
		presenter.openEditDialog(sectionTreeComponent);
		verify(view, times(1)).openEditBranchDialog(
				argThat(new BranchMatcher(fakeBranch)));
		assertEquals(presenter.getCurrentSectionTreeComponent(),
				sectionTreeComponent);

	}

	@Test
	public void testOpenNewSectionDialog() {
		List<Section> fakeSections = getFakeSections(9);
		when(service.getAll()).thenReturn(fakeSections);
		presenter.initView(view);

		presenter.openNewSectionDialog();
		verify(view, times(1)).openNewSectionDialog();
	}

	@Test
	public void testOpenNewBranchDialog() {
		List<Section> fakeSections = getFakeSections(9);
		when(service.getAll()).thenReturn(fakeSections);
		presenter.initView(view);

		SectionTreeComponent sectionTreeComponent = mock(SectionTreeComponent.class);

		presenter.openNewBranchDialog(sectionTreeComponent);
		assertEquals(presenter.getCurrentSectionTreeComponent(),
				sectionTreeComponent);
		verify(view, times(1)).openNewBranchDialog();

	}

	@Test
	public void testRemoveSectionFromView() {
		List<Section> fakeSections = getFakeSections(9);
		when(service.getAll()).thenReturn(fakeSections);
		presenter.initView(view);

		presenter.removeSectionFromView(null);
		verify(view, never()).removeSection(
				argThat(new SectionMatcher(fakeSections.get(4))));

		presenter.removeSectionFromView(fakeSections.get(4));
		verify(view, times(1)).removeSection(
				argThat(new SectionMatcher(fakeSections.get(4))));
	}

	@Test
	public void testEditSection() {
		List<Section> fakeSections = getFakeSections(9);
		Section section1 = getFakeSection("1", "");
		when(service.getAll()).thenReturn(fakeSections);
		when(service.isSectionExists(section1)).thenReturn(true);
		presenter.initView(view);

		SectionTreeComponent sectionTreeComponent = mock(SectionTreeComponent.class);
		when(sectionTreeComponent.getSelectedObject()).thenReturn(
				fakeSections.get(3));

		presenter.openEditDialog(sectionTreeComponent);
		presenter.editSection("1", "2");

		verify(dialogManager).confirmEdition(anyString(),
				argThat(new PerformableMatcher(new DialogManager.Performable() {
					@Override
					public void execute() {
					}
				})));

		presenter.editSection("", "2");
		verify(view, times(1)).openErrorPopupInEditSectionDialog(
				SectionPresenter.ERROR_LABEL_SECTION_NAME_CANT_BE_VOID);

		presenter.editSection(null, "2");
		verify(view, times(2)).openErrorPopupInEditSectionDialog(
				SectionPresenter.ERROR_LABEL_SECTION_NAME_CANT_BE_VOID);
	}

	@Test
	public void testAddNewSection() {
		
		List<Section> fakeSections = getFakeSections(9);
		//Section section = getFakeSection(anyString(), "BBB");
		when(service.getAll()).thenReturn(fakeSections);
		presenter.initView(view);		
		when(service.isSectionExists(any(Section.class))).thenReturn(true).thenReturn(
				false);

		try {
			presenter.addNewSection(null, null);
			verify(view).openErrorPopupInNewSectionDialog(
					SectionPresenter.ERROR_LABEL_SECTION_NAME_CANT_BE_VOID);
			 verify(service, never()).saveSection(any(Section.class));
		} catch (NotUniqueException e) {
			fail("Can't be thrown because saveSection should not has been invoked");
		}

		try {
			presenter.addNewSection("", "");
			verify(view, times(2)).openErrorPopupInNewSectionDialog(
					SectionPresenter.ERROR_LABEL_SECTION_NAME_CANT_BE_VOID);
			verify(service, never()).saveSection(
					argThat(new SectionMatcher(new Section())));
		} catch (NotUniqueException e) {
			fail("Can't be thrown because saveSection should not has been invoked");
		}

		presenter.addNewSection("AAA1", "BBB");
		verify(dialogManager, never()).confirmCreation(anyString(),
				argThat(new PerformableMatcher(new DialogManager.Performable() {
					@Override
					public void execute() {
					}
				})));

		presenter.addNewSection("AAA", "BBB");
		verify(dialogManager).confirmCreation(anyString(),
				argThat(new PerformableMatcher(new DialogManager.Performable() {
					@Override
					public void execute() {
					}
				})));		 
	}

	@Test
	public void testDeleteSection() {
		List<Section> fakeSections = getFakeSections(9);
		when(service.getAll()).thenReturn(fakeSections);
		SectionTreeComponent sectionTreeComponent = mock(SectionTreeComponent.class);
		when(sectionTreeComponent.getSelectedObject()).thenReturn(null)
				.thenReturn(null).thenReturn(getFakeBranch("test", "test"))
				.thenReturn(getFakeBranch("test", "test"))
				.thenReturn(fakeSections.get(3))
				.thenReturn(fakeSections.get(3));

		presenter.initView(view);
		presenter.setCurrentSectionTreeComponent(sectionTreeComponent);

		// null object
		presenter.deleteSection(null);
		verify(dialogManager, never()).confirmDeletion(anyString(),
				argThat(new PerformableMatcher(new DialogManager.Performable() {
					@Override
					public void execute() {
					}
				})));
		presenter.deleteSection(getFakeSection("test", "test"));
		verify(dialogManager, never()).confirmDeletion(anyString(),
				argThat(new PerformableMatcher(new DialogManager.Performable() {
					@Override
					public void execute() {
					}
				})));

		// unproper object
		presenter.deleteSection(null);
		verify(dialogManager, never()).confirmDeletion(anyString(),
				argThat(new PerformableMatcher(new DialogManager.Performable() {
					@Override
					public void execute() {
					}
				})));
		presenter.deleteSection(getFakeSection("test", "test"));
		verify(dialogManager, never()).confirmDeletion(anyString(),
				argThat(new PerformableMatcher(new DialogManager.Performable() {
					@Override
					public void execute() {
					}
				})));

		// proper object
		presenter.deleteSection(null);
		verify(dialogManager).confirmDeletion(anyString(),
				argThat(new PerformableMatcher(new DialogManager.Performable() {
					@Override
					public void execute() {
					}
				})));
		
		presenter.deleteSection(getFakeSection("test", "test"));
		verify(dialogManager, times(2)).confirmDeletion(anyString(),
				argThat(new PerformableMatcher(new DialogManager.Performable() {
					@Override
					public void execute() {
					}
				})));

	}

	@Test
	public void testCreateUpdatePerformableCreateSection() {
		List<Section> fakeSections = getFakeSections(9);
		Section test = getFakeSection("test", "");
		when(service.getAll()).thenReturn(fakeSections);
		when(service.isSectionExists(test)).thenReturn(false);
		presenter.initView(view);
		CreateUpdatePerformable perf = presenter.new CreateUpdatePerformable(
				null, "test", "test");
		perf.execute();
		verify(view).showSection(argThat(new SectionMatcher("test", "test")));
		verify(view).closeNewSectionDialog();
		try {
			verify(service).saveSection(
					argThat(new SectionMatcher("test", "test")));
		} catch (NotUniqueException e) {
			fail("Can't be thrown. The mock object handles this situation");
		}

	}

	@Test
	public void testCreateUpdatePerformableEditSection() {
		List<Section> fakeSections = getFakeSections(9);
		when(service.getAll()).thenReturn(fakeSections);
		presenter.initView(view);
		SectionTreeComponent sectionTreeComponent = mock(SectionTreeComponent.class);
		presenter.setCurrentSectionTreeComponent(sectionTreeComponent);
		CreateUpdatePerformable perf = presenter.new CreateUpdatePerformable(
				fakeSections.get(3), "test", "test");
		perf.execute();

		verify(sectionTreeComponent).updateSectionInView(
				argThat(new SectionMatcher("test", "test")));
		verify(view).closeEditSectionDialog();
		try {
			verify(service).saveSection(
					argThat(new SectionMatcher("test", "test")));
		} catch (NotUniqueException e) {
			fail("Can't be thrown. The mock object handles this situation");
		}

	}

	@Test
	public void testDeleteBranchPerformable() {
		List<Section> fakeSections = getFakeSections(9);
		when(service.getAll()).thenReturn(fakeSections);
		presenter.initView(view);

		Section fakeSection = fakeSections.get(3);
		Branch fakeBranch = getFakeBranch("test 1", "test 1");
		Branch fakeBranch_1 = getFakeBranch("test 2", "test 2");
		fakeBranch.setSection(fakeSection);
		fakeBranch_1.setSection(fakeSection);
		fakeSection.addBranch(fakeBranch);
		fakeSection.addBranch(fakeBranch_1);

		DeleteBranchPerformable perf = presenter.new DeleteBranchPerformable(
				fakeBranch);
		perf.execute();

		assertEquals(fakeSection.getBranches().size(), 1);
		assertEquals(fakeSection.getBranches().get(0), fakeBranch_1);
		try {
			verify(service).saveSection(
					argThat(new SectionMatcher(fakeSections.get(3))));
		} catch (NotUniqueException e) {
			fail("Can't be thrown. The mock object handles this situation");
		}		
	}
	
	@Test
	public void testDeleteSectionSaveBranchesPerformable(){
		List<Section> fakeSections = getFakeSections(9);
		when(service.getAll()).thenReturn(fakeSections);
		presenter.initView(view);
		
		Section fakeSection = fakeSections.get(3);
		Branch fakeBranch = getFakeBranch("test 1", "test 1");
		Branch fakeBranch_1 = getFakeBranch("test 1", "test 1");
		fakeBranch.setSection(fakeSection);
		fakeBranch_1.setSection(fakeSection);
		fakeSection.addBranch(fakeBranch);
		fakeSection.addBranch(fakeBranch_1);
		
		Section fakeSection_1 = fakeSections.get(3);
		Branch fakeBranch_2 = getFakeBranch("test 1", "test 1");
		Branch fakeBranch_3 = getFakeBranch("test 1", "test 1");
		fakeBranch_2.setSection(fakeSection_1);
		fakeBranch_3.setSection(fakeSection_1);
		fakeSection_1.addBranch(fakeBranch_2);
		fakeSection_1.addBranch(fakeBranch_3);
		
		DeleteSectionPerformable perf = presenter.new DeleteSectionPerformable(fakeSection, fakeSection_1);
		
		perf.execute();
		verify(service).deleteAndMoveBranchesTo(argThat(new SectionMatcher(fakeSection)), argThat(new SectionMatcher(fakeSection_1)));		
		assertEquals(fakeSection_1.getBranches().size(), 4);
		assertTrue(fakeSection_1.getBranches().contains(fakeBranch));
		assertTrue(fakeSection_1.getBranches().contains(fakeBranch_1));
		assertTrue(fakeSection_1.getBranches().contains(fakeBranch_2));
		assertTrue(fakeSection_1.getBranches().contains(fakeBranch_3));
	}
	
	@Test
	public void testDeleteSectionWithoutSaveBranchesPerformable(){
		List<Section> fakeSections = getFakeSections(9);
		when(service.getAll()).thenReturn(fakeSections);
		presenter.initView(view);
		
		Section fakeSection = fakeSections.get(3);
		Branch fakeBranch = getFakeBranch("test 1", "test 1");
		Branch fakeBranch_1 = getFakeBranch("test 1", "test 1");
		fakeBranch.setSection(fakeSection);
		fakeBranch_1.setSection(fakeSection);
		fakeSection.addBranch(fakeBranch);
		fakeSection.addBranch(fakeBranch_1);
		
		Section fakeSection_1 = fakeSections.get(3);
		Branch fakeBranch_2 = getFakeBranch("test 1", "test 1");
		Branch fakeBranch_3 = getFakeBranch("test 1", "test 1");
		fakeBranch_2.setSection(fakeSection_1);
		fakeBranch_3.setSection(fakeSection_1);
		fakeSection_1.addBranch(fakeBranch_2);
		fakeSection_1.addBranch(fakeBranch_3);
		
		DeleteSectionPerformable perf = presenter.new DeleteSectionPerformable(fakeSection,null);
		
		perf.execute();
		verify(service).deleteRecursively(argThat(new SectionMatcher(fakeSection)));
	}

	

}
