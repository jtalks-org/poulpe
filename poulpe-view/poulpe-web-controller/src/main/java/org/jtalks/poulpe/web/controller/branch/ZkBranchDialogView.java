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
package org.jtalks.poulpe.web.controller.branch;

import java.util.List;

import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.validator.ValidationFailureHandler;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * This class is implementation view for single branch
 * 
 * @author Bekrenev Dmitry
 * @author Vyacheslav Zhivaev
 */
public class ZkBranchDialogView extends Window implements BranchDialogView, AfterCompose {

    private static final long serialVersionUID = 7388638074018815713L;

    private BranchPresenter presenter;
    private Textbox branchName;
    private Textbox branchDescription;
    private Button confirmButton;
    private Button rejectButton;
    private PoulpeBranch branch;
    private Combobox sectionList;

    private ZkHelper zkHelper = new ZkHelper(this);

    private ValidationFailureHandler validationHandler;

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterCompose() {
        zkHelper.wireByConvention();

        presenter.setView(this);
        presenter.initView();

        sectionList.setItemRenderer(sectionItemRenderer);

        validationHandler = new ValidationFailureHandler("name", branchName);
    }

    /**
     * set default section in combobox for select section when branch will be
     * stored
     * 
     * @param section default section
     */
    private void setDefaultSection(PoulpeSection section) {
        @SuppressWarnings("unchecked")
        ListModelList<PoulpeSection> model = (ListModelList<PoulpeSection>) sectionList.getModel();
        model.clearSelection();
        if (section != null) {
            model.addToSelection(section);
        } else {
            model.addToSelection(model.get(0));
        }
        sectionList.setModel(model);

    }

    /**
     * This class render items Combobox list
     */
    private static ComboitemRenderer<PoulpeSection> sectionItemRenderer = new ComboitemRenderer<PoulpeSection>() {

        @Override
        public void render(Comboitem item, PoulpeSection section, int index) {
            item.setLabel(section.getName());
            item.setDescription(section.getDescription());
        }
    };

    /**
     * Set presenter
     * 
     * @see BranchEditorPresenter
     * @param presenter instance presenter for view
     */
    public void setPresenter(BranchPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Handle event when user click on confirm button
     */
    public void onClick$confirmButton() {
        branchName.setConstraint("no empty");
        presenter.saveBranch();
        presenter.updateView();
    }

    /**
     * Handle event when user click on reject button
     */
    public void onClick$rejectButton() {
        hide();
    }

    /**
     * Handle event when branch name field in focus
     */
    public void onFocus$branchName() {
        branchName.clearErrorMessage();
    }

    /**
     * Handle event from main window for open add new branch dialog
     */
    public void onOpenAddDialog() {
        show();
    }

    /**
     * Handle event from main window for open edit branch dialog
     * @param event information about event
     */
    public void onOpenEditDialog(Event event) {
        show((PoulpeBranch) event.getData());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PoulpeSection getSection() {
        PoulpeSection section = (PoulpeSection) sectionList.getModel().getElementAt(sectionList.getSelectedIndex());
        return section;
    }

    public PoulpeBranch getBranch(PoulpeSection section) {
        branch.setName(branchName.getText().trim());
        branch.setDescription(branchDescription.getText().trim());
        branch.setSection(section);
        return branch;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide() {
        setVisible(false);
        BranchEditorView view = (BranchEditorView) getAttribute("backWin");
        if (view == null) {
            return;
        }
        BranchEditorPresenter presenter = (BranchEditorPresenter) getAttribute("presenter");
        presenter.updateView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        presenter.initView();
        setTitle(Labels.getLabel("branches.newbranchedialog.title"));
        confirmButton.setLabel(Labels.getLabel("branches.button.add"));
        rejectButton.setLabel(Labels.getLabel("branches.button.cancel"));
        branchName.setRawValue("");
        branchName.setConstraint("");
        branchDescription.setText("");
        branch = new PoulpeBranch();
        setDefaultSection(null);
        setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(PoulpeBranch branch) {
        presenter.initView();
        setTitle(Labels.getLabel("branches.editdialog.title"));
        confirmButton.setLabel(Labels.getLabel("branches.button.edit"));
        rejectButton.setLabel(Labels.getLabel("branches.button.cancel"));
        PoulpeSection section = branch.getPoulpeSection();
        setDefaultSection(section);
        branchName.setText(branch.getName());
        branchName.setConstraint("");
        branchDescription.setText(branch.getDescription());
        this.branch = branch;
        setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initSectionList(List<PoulpeSection> sections) {
        sectionList.setModel(new ListModelList<PoulpeSection>(sections));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void openErrorPopupInNewSectionDialog(String label) {
        String message = Labels.getLabel(label);
        branchName.setErrorMessage(message);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validationFailure(ValidationResult result) {
        validationHandler.validationFailure(result);
    }

}
