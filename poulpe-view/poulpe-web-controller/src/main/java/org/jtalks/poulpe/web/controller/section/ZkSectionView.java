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

import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.validator.ValidationFailure;
import org.jtalks.poulpe.validator.ValidationFailureHandler;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import java.util.List;

/**
 * Zk-based implementation of {@link SectionView}
 * 
 * @author unascribed
 */
public class ZkSectionView extends Window implements AfterCompose, SectionView, ValidationFailure {

    private static final long serialVersionUID = -2745900622179593542L;

    private SectionPresenter presenter;

    private ZkHelper zkHelper = new ZkHelper(this);
    private TreeComponentFactory treeComponentFactory;
    private ValidationFailureHandler handler;

    private Window editSectionDialog;
    private Textbox editSectionDialog$sectionName;
    private Textbox editSectionDialog$sectionDescription;

    // TODO: get rid of it - use Strategy
    private boolean forEditing = false;

    /** {@inheritDoc} */
    @Override
    public void afterCompose() {
        zkHelper.wireByConvention();
        
        handler = new ValidationFailureHandler("name", editSectionDialog$sectionName);
        treeComponentFactory = new TreeComponentFactory(presenter);
        
        presenter.initView(this);
        Button button = new Button("+");
        button.setId("addSectionButtonBottom");
        button.addEventListener("onClick", new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				openNewSectionDialog();
			}
		});
        zkHelper.addComponent(button);
        hide();
    }

    /** {@inheritDoc} */
    @Override
    public void validationFailure(ValidationResult result) {
        handler.validationFailure(result);
    }

    /** {@inheritDoc} */
    @Override
    public void addSection(PoulpeSection section) {
        zkHelper.addComponent(treeComponentFactory.sectionTreeComponent(section));
    }

    /** {@inheritDoc} */
    @Override
    public void addSections(List<PoulpeSection> sections) {
        removeOldSections();
        
        for (PoulpeSection section : sections) {
            addSection(section);
        }
    }

    /**
     * Removes all sections and doesn't do anything with another components 
     */
    private void removeOldSections() {
        zkHelper.removeAll(ZkSectionTreeComponent.class);
    }

    /** {@inheritDoc} */
    @Override
    public void removeSection(PoulpeSection section) {
        // TODO ???
    }

    /** {@inheritDoc} */
    @Override
    public void openNewSectionDialog() {
        forEditing = false;
        clean();
        show();
    }

    /**
     * Displays edit section dialog
     */
    private void show() {
        editSectionDialog.setVisible(true);
    }

    /** {@inheritDoc} */
    @Override
    public void openEditSectionDialog(PoulpeSection section) {
        forEditing = true;
        setData(section.getName(), section.getDescription());
        show();
    }

    /**
     * @param name text to be put to sectionName textbox
     * @param description text to be put to sectionDescription textbox
     */
    private void setData(String name, String description) {
        editSectionDialog$sectionName.setText(name);
        editSectionDialog$sectionDescription.setText(description);
    }

    /**
     * Saves or creates a {@link PoulpeSection}, depending on what 
     * action was called. If {@link #openNewSectionDialog()}, then {@link #createSection()}
     * is called, if {@link #openEditSectionDialog(PoulpeSection)}, then - {@link #saveSection()}
     */
    public void save() {
        if (forEditing) {
            saveSection();
        } else {
            createSection();
        }
    }

    /**
     * Saves new section
     */
    private void createSection() {
        presenter.addNewSection(getSectionName(), getSectionDescription());
    }

    /**
     * Updates old section
     */
    private void saveSection() {
        presenter.editSection(getSectionName(), getSectionDescription());
    }

    /**
     * Hides editSectionDialog window
     */
    private void hide() {
        editSectionDialog.setVisible(false);
    }

    /** {@inheritDoc} */
    @Override
    public void closeEditSectionDialog() {
        hide();
        clean();
    }

    /**
     * Cleans input data for displaying editSectionDialog for creating
     */
    private void clean() {
        setData("", "");
    }

    /**
     * @return text value for sectionName textbox
     */
    private String getSectionName() {
        return editSectionDialog$sectionName.getText();
    }

    /**
     * @return text value for sectionDescription textbox
     */
    private String getSectionDescription() {
        return editSectionDialog$sectionDescription.getText();
    }

    /**
     * Handle event when child dialog was hiding
     */
    public void onHideDialog() {
        presenter.updateView();
    }

    // UI EVENT HANDLERS

    /**
     * Event which happen when user click on '+' buttton opens dialog for adding
     * new section
     */
    public void onClick$addSectionButton() {
        openNewSectionDialog();
    }

    /**
     * Event which happen when user click on add button in new branch dialog
     * window this cause save new branch
     */
    public void onClick$editButton$editSectionDialog() {
        save();
    }

    /**
     * Event which happen when user click on cancel button in new branch dialog
     * window this cause close new branch dialog
     */
    public void onClick$closeButton$editSectionDialog() {
        closeEditSectionDialog();
    }

    // CUSTOM EVENT HANDLER

    /**
     * @deprecated use {@link DialogManager} and
     * {@link PerfomableFactory#deleteSection(PoulpeSection,PoulpeSection)} for this
     */
    @Deprecated
    public void openDeleteSectionDialog(PoulpeSection victim) {
        Events.postEvent(new Event("onOpenDeleteSectionDialog", getDesktop().getPage("sectionDeleteDialog").getFellow(
                "deleteWindow"), victim));
    }
    
    /** {@inheritDoc} */
    @Override
    @Deprecated
    public void openNewBranchDialog() {
        Events.postEvent(new Event("onOpenAddDialog", getDesktop().getPage("BranchDialog").getFellow("editWindow")));
    }
    
    /** {@inheritDoc} */
    @Override
    @Deprecated
    public void openEditBranchDialog(PoulpeBranch branch) {
        Events.postEvent(new Event("onOpenEditDialog", getDesktop().getPage("BranchDialog").getFellow("editWindow"),
                branch));
    }
    
    /** {@inheritDoc} */
    @Override
    @Deprecated
    public void openModerationDialog(PoulpeBranch branch) {
        Events.postEvent(new Event("onOpen", getDesktop().getPage("moderatorDialog").getFellow("moderatorWindow"),
                branch));
    }

    @Deprecated
    public void closeModeratorDialog() {
        Events.postEvent(new Event("onCloseModeratorDialog", getDesktop().getPage("moderatorDialog").getFellow(
                "moderatorWindow")));
    }

    // SETTERS

    /**
     * @param presenter instance presenter for view
     */
    public void setPresenter(SectionPresenter presenter) {
        this.presenter = presenter;
    }
    
    /**
     * Package-private for DI in tests
     * @param handler validator instance
     * @param zkHelper helper instance
     * @param treeComponentFactory
     */
    void setTestDependencies(ValidationFailureHandler handler, ZkHelper zkHelper, TreeComponentFactory treeComponentFactory) {
        this.handler = handler;
        this.zkHelper = zkHelper;
        this.treeComponentFactory = treeComponentFactory;
    }

    /**
     * Package-private for DI in tests
     * @param editSectionDialog window which holds textboxes
     * @param sectionName textbox with section name
     * @param sectionDescription textbox with section description
     */
    void setUiElements(Window editSectionDialog, Textbox sectionName, Textbox sectionDescription) {
        this.editSectionDialog = editSectionDialog;
        this.editSectionDialog$sectionName = sectionName;
        this.editSectionDialog$sectionDescription = sectionDescription;
    }
}

/**
 * Factory for initializing TreeComponentFactory, used in tests
 * @author Alexey Grigorev
 */
class TreeComponentFactory {
    private final SectionPresenter presenter;

    /**
     * @param presenter to be passed to new {@link ZkSectionTreeComponent}s
     */
    public TreeComponentFactory(SectionPresenter presenter) {
        this.presenter = presenter;
    }
    
    /**
     * @param section for which component is created
     * @return {@link ZkSectionTreeComponent} instance
     */
    public ZkSectionTreeComponent sectionTreeComponent(PoulpeSection section) {
        return new ZkSectionTreeComponent(section, presenter);
    }
}