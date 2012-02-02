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

import java.util.List;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.validation.ValidationResult;
import org.jtalks.poulpe.validator.ValidationFailure;
import org.jtalks.poulpe.validator.ValidationFailureHandler;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * 
 * @author unascribed
 * @author Alexey Grigorev
 */
public class ZkSectionView extends Window implements AfterCompose, SectionView, ValidationFailure {

    private static final long serialVersionUID = -2745900622179593542L;

    private ZkHelper zkHelper = new ZkHelper(this);
    private ValidationFailureHandler handler;

    private SectionPresenter presenter;

    private Window editSectionDialog;
    private Textbox editSectionDialog$sectionName;
    private Textbox editSectionDialog$sectionDescription;

    private boolean forEditing = false;

    @Override
    public void afterCompose() {
        zkHelper.wireByConvention();
        handler = new ValidationFailureHandler("name", editSectionDialog$sectionName);
        presenter.initView(this);
    }

    @Override
    public void validationFailure(ValidationResult result) {
        handler.validationFailure(result);
    }

    @Override
    public void showSection(Section section) {
        addSection(section);
    }

    @Override
    public void showSections(List<Section> sections) {
        removeOldSections();
        addSections(sections);
    }

    private void removeOldSections() {
        List<Component> childrenToSave = zkHelper.filterOut(ZkSectionTreeComponent.class);
        zkHelper.removeAllChildComponents();
        zkHelper.addComponents(childrenToSave);
    }

    private void addSections(List<Section> sections) {
        for (Section section : sections) {
            addSection(section);
        }
    }

    private void addSection(Section section) {
        // TODO move SectionTreeComponent creation to external factory method
        zkHelper.addComponent(new ZkSectionTreeComponent(section, presenter));
    }

    @Override
    public void removeSection(Section section) {
    }

    @Override
    public void openNewSectionDialog() {
        forEditing = false;
        clean();
        show();
    }

    private void show() {
        editSectionDialog.setVisible(true);
    }

    @Override
    public void openEditSectionDialog(String name, String description) {
        forEditing = true;
        setData(name, description);
        show();
    }

    private void setData(String name, String description) {
        editSectionDialog$sectionName.setText(name);
        editSectionDialog$sectionDescription.setText(description);
    }

    private void save() {
        if (forEditing) {
            saveSection();
        } else {
            createSection();
        }
    }

    private void createSection() {
        presenter.addNewSection(getSectionName(), getSectionDescription());
    }

    private void saveSection() {
        presenter.editSection(getSectionName(), getSectionDescription());
    }

    @Override
    @Deprecated
    public void closeNewSectionDialog() {
        closeEditSectionDialog();
    }

    private void hide() {
        editSectionDialog.setVisible(false);
    }

    @Override
    public void closeEditSectionDialog() {
        hide();
        clean();
    }

    private void clean() {
        setData("", "");
    }

    private String getSectionName() {
        return editSectionDialog$sectionName.getText();
    }

    private String getSectionDescription() {
        return editSectionDialog$sectionDescription.getText();
    }

    @Override
    public void closeDialogs() {
        closeEditSectionDialog();
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
     * {@link PerfomableFactory#deleteSection(Section)} for this
     */
    @Deprecated
    public void openDeleteSectionDialog(Section victim) {
        Events.postEvent(new Event("onOpenDeleteSectionDialog", getDesktop().getPage("sectionDeleteDialog").getFellow(
                "deleteWindow"), victim));
    }

    @Override
    @Deprecated
    public void openNewBranchDialog() {
        Events.postEvent(new Event("onOpenAddDialog", getDesktop().getPage("BranchDialog").getFellow("editWindow")));
    }

    @Override
    @Deprecated
    public void openEditBranchDialog(Branch branch) {
        Events.postEvent(new Event("onOpenEditDialog", getDesktop().getPage("BranchDialog").getFellow("editWindow"),
                branch));
    }

    @Override
    @Deprecated
    public void openModerationDialog(Branch branch) {
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

    void setValidationFailureHandler(ValidationFailureHandler handler) {
        this.handler = handler;
    }

    void setZkHelper(ZkHelper zkHelper) {
        this.zkHelper = zkHelper;
    }

}
