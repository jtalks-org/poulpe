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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.web.controller.branch.BranchPresenter;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class SectionViewImpl extends Window implements SectionView,
        AfterCompose {

    private static final long serialVersionUID = -2745900622179593542L;
    private transient SectionPresenter presenter;

    private Window newSectionDialog;
    private Textbox newSectionDialog$sectionName;
    private Textbox newSectionDialog$sectionDescription;
    private Window editSectionDialog;
    private Textbox editSectionDialog$sectionName;
    private Textbox editSectionDialog$sectionDescription;

    /**
     * {@inheritDoc}
     * */
    @Override
    public void afterCompose() {
        Components.addForwards(this, this);
        Components.wireVariables(this, this);

        presenter.initView(this);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void showSection(Section section) {
        // TODO move SectionTreeComponent creation to external factory method
        getChildren().add(new SectionTreeComponentImpl(section, presenter));
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void showSections(List<Section> sections) {
        //TODO: Find out what's happening here.
        List<Object> childrenToSave = new ArrayList<Object>();
        for (Object obj : getChildren()) {
            if (!(obj instanceof SectionTreeComponent)) {
                childrenToSave.add(obj);
            }
        }
        getChildren().clear();
        for (Object obj : childrenToSave) {
            getChildren().add((Component) obj);
        }
        for (Section section : sections) {
            // TODO move SectionTreeComponent creation to external factory
            // method
            getChildren().add(new SectionTreeComponentImpl(section, presenter));
        }
    }

    /**
     * Set presenter
     * 
     * @see BranchPresenter
     * @param presenter
     *            instance presenter for view
     * */
    public void setPresenter(SectionPresenter presenter) {
        this.presenter = presenter;
    }

    // EVENT HANDLERS

    /**
     * Event which happen when user click on '+' buttton opens dialog for adding
     * new section
     * */
    public void onClick$addSectionButton() {
        presenter.openNewSectionDialog();
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void removeSection(Section section) {
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void openNewSectionDialog() {
        setNewSectionName("");
        setNewSectionDescription("");
        newSectionDialog.setVisible(true);
    }

    /**
     * Event which happen when user click on cancel button in new branch dialog
     * window this cause close new branch dialog
     * */
    public void onClick$closeButton$newSectionDialog() {
        closeNewSectionDialog();
    }

    /**
     * Event occurs when the used click on cancel button in the
     * DeleteSectionDialog
     */
    public void onClick$closeButton$deleteSectionDialog() {
        closeDeleteSectionDialog();
    }

    /**
     * Event occurs when the used click on delete button in the
     * DeleteSectionDialog
     */
    public void onClick$deleteButton$deleteSectionDialog() {
        // if (deleteSectionDialog$deleteAll.isChecked()) {
        // presenter.deleteSection(null);
        // } else if (deleteSectionDialog$transferAll.isChecked()) {
        // Object selectedObject = deleteSectionDialog$sectionsCombobox
        // .getModel().getElementAt(
        // deleteSectionDialog$sectionsCombobox
        // .getSelectedIndices());
        // presenter.deleteSection((Section) selectedObject);
        // }
    }

    /**
     * Event which happen when user click on add button in new branch dialog
     * window this cause save new branch
     * */
    public void onClick$addButton$newSectionDialog() {
        presenter
                .addNewSection(getNewSectionName(), getNewSectionDescription());
    }

    /**
     * Event which happen when user click on cancel button in new branch dialog
     * window this cause close new branch dialog
     * */
    public void onClick$closeButton$editSectionDialog() {
        closeEditSectionDialog();
    }

    /**
     * Event which happen when user click on add button in new branch dialog
     * window this cause save new branch
     * */
    public void onClick$editButton$editSectionDialog() {
        presenter
                .editSection(getEditSectionName(), getEditSectionDescription());

    }

    // public void onClick$deleteAll$deleteSectionDialog() {
    // if (deleteSectionDialog$deleteAll.isChecked()) {
    // deleteSectionDialog$sectionsCombobox.setDisabled(true);
    // }
    // }
    //
    // public void onClick$transferAll$deleteSectionDialog() {
    // if (deleteSectionDialog$transferAll.isChecked()) {
    // deleteSectionDialog$sectionsCombobox.setDisabled(false);
    // }
    // }

    /** {@inheritDoc} */
    @Override
    public void closeNewSectionDialog() {
        newSectionDialog.setVisible(false);
        setNewSectionDescription("");
        setNewSectionName("");
    }

    /** {@inheritDoc} */
    @Override
    public void closeEditSectionDialog() {
        editSectionDialog.setVisible(false);
        editSectionDialog$sectionName.setText("");
        editSectionDialog$sectionDescription.setText("");
    }

    /** {@inheritDoc} */
    @Override
    public void closeDeleteSectionDialog() {
        // deleteSectionDialog$transferAll.setChecked(true);
        // deleteSectionDialog$sectionsCombobox.setModel(new ListModelList());
        // deleteSectionDialog.setVisible(false);
    }

    /** {@inheritDoc} */
    @Override
    public void openDeleteSectionDialog(Section victim) {
        Events.postEvent(new Event("onOpenDeleteSectionDialog", getDesktop()
                .getPage("sectionDeleteDialog").getFellow("deleteWindow"), victim));
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void openEditSectionDialog(String name, String description) {
        setEditSectionName(name);
        setEditSectionDescription(description);
        editSectionDialog.setVisible(true);
    }

    /**
     * get the value of sectionName textField of newSectionDialog
     * 
     * @return new section name
     */
    private String getNewSectionName() {
        return newSectionDialog$sectionName.getText();
    }

    /**
     * get the value of sectionDescription textField of newSectionDialog
     * 
     * @return new section description
     */
    private String getNewSectionDescription() {
        return newSectionDialog$sectionDescription.getText();
    }

    /**
     * @return edit section name
     * */
    private String getEditSectionName() {
        return editSectionDialog$sectionName.getText();
    }

    /**
     * @return edit section description
     * */
    private String getEditSectionDescription() {
        return editSectionDialog$sectionDescription.getText();
    }

    /**
     * @param name
     *            for edit section
     * */
    private void setEditSectionName(String name) {
        editSectionDialog$sectionName.setText(name);
    }

    /**
     * @param description
     *            for edit section
     * */
    private void setEditSectionDescription(String description) {
        editSectionDialog$sectionDescription.setText(description);
    }

    /**
     * @param name
     *            for new section
     * */
    private void setNewSectionName(String name) {
        newSectionDialog$sectionName.setText(name);
    }

    /**
     * @param description
     *            for new section
     * */
    private void setNewSectionDescription(String description) {
        newSectionDialog$sectionDescription.setText(description);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void openErrorPopupInNewSectionDialog(String label) {
        final String message = Labels.getLabel(label);
        newSectionDialog$sectionName.setErrorMessage(message);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void openErrorPopupInEditSectionDialog(String label) {
        final String message = Labels.getLabel(label);
        editSectionDialog$sectionName.setErrorMessage(message);
    }

    /**
     * {@inheritDoc}
     * */

    @Override
    public void closeDialogs() {
        closeNewSectionDialog();
        closeEditSectionDialog();
        closeDeleteSectionDialog();
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void openNewBranchDialog() {
        Events.postEvent(new Event("onOpenAddDialog", getDesktop().getPage(
                "BranchDialog").getFellow("editWindow")));
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void closeNewBranchDialog() {
        // TODO : implement
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void openEditBranchDialog(Branch branch) {
        Events.postEvent(new Event("onOpenEditDialog", getDesktop().getPage(
                "BranchDialog").getFellow("editWindow"), branch));
    }

    @Override
    public void openModeratorDialog(Branch branch) {
        Events.postEvent(new Event("onOpen", getDesktop()
                .getPage("moderatorDialog").getFellow("moderatorWindow"),
                branch));
    }

    public void forwardToBranchPermissionManagement(Branch branch) throws IOException {

    }

    @Override
    public void closeModeratorDialog() {
        Events.postEvent(new Event("onCloseModeratorDialog", getDesktop()
                .getPage("moderatorDialog").getFellow("moderatorWindow")));
    }
   

    /**
     * {@inheritDoc}
     * */
    @Override
    public void openDeleteBranchDialog(Branch branch) {

    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void closeEditBranchDialog() {

    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public boolean isDeleteDialogOpen() {
        return false;
        // return deleteSectionDialog.isVisible();
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public boolean isEditDialogOpen() {
        return editSectionDialog.isVisible();
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public boolean isNewDialogOpen() {
        return newSectionDialog.isVisible();
    }

    /**
     * Handle event when child dialog was hiding
     * */
    public void onHideDialog() {
        presenter.updateView();
    }

}
