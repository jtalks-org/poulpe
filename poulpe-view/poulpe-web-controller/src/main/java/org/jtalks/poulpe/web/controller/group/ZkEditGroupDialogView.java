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
package org.jtalks.poulpe.web.controller.group;

import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.model.entity.PoulpeGroup;
import org.jtalks.poulpe.validator.ValidationFailureHandler;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * This class is implementation view for single branch
 * 
 * @author Bekrenev Dmitry
 * @author Vyacheslav Zhivaev
 */
public class ZkEditGroupDialogView extends Window implements EditGroupDialogView, AfterCompose {

    private static final long serialVersionUID = 7388638074018815713L;

    private EditGroupDialogPresenter presenter;
    private Textbox groupName;
    private Textbox groupDescription;
    private Button confirmButton;
    private Button rejectButton;

    // private Combobox sectionList;

    private ValidationFailureHandler handler;
    
    private ZkHelper zkHelper = new ZkHelper(this);
    /**
     * {@inheritDoc}
     * */
    @Override
    public void afterCompose() {
        zkHelper.wireByConvention();
        
        presenter.initView(this, null);

        handler = new ValidationFailureHandler("name", groupName);
    }

    @Override
    public void validationFailure(ValidationResult result) {
        handler.validationFailure(result);
    }

    /**
     * Handle event when user click on confirm button
     */
    public void onClick$confirmButton() {
        groupName.setConstraint("no empty");
        presenter.saveOrUpdateGroup(groupName.getText(), groupDescription.getText());
    }

    /**
     * Handle event when user click on reject button
     * */
    public void onClick$rejectButton() {
        hide();
    }

    /**
     * Handle event when branch name field in focus
     */
    public void onFocus$branchName() {
        groupName.clearErrorMessage();
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
        show((PoulpeGroup) event.getData());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide() {
        setVisible(false);
        ZkGroupView view = (ZkGroupView) getAttribute("backWindow");
        if (view == null) {
            return;
        }
        GroupPresenter presenter = (GroupPresenter) getAttribute("presenter");
        presenter.updateView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        presenter.initView(this, null);
        setTitle(Labels.getLabel("groups.newbranchedialog.title"));
        confirmButton.setLabel(Labels.getLabel("groups.button.add"));
        rejectButton.setLabel(Labels.getLabel("groups.button.cancel"));
        groupName.setRawValue("");
        groupName.setConstraint("");
        groupDescription.setText("");
        setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(PoulpeGroup group) {
        presenter.initView(this, group);
        setTitle(Labels.getLabel("groups.editdialog.title"));
        confirmButton.setLabel(Labels.getLabel("groups.button.edit"));
        rejectButton.setLabel(Labels.getLabel("groups.button.cancel"));
        groupName.setText(group.getName());
        groupName.setConstraint("");
        groupDescription.setText(group.getDescription());
        setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void openErrorPopupInEditGroupDialog(String label) {
        final String message = Labels.getLabel(label);
        groupName.setErrorMessage(message);
    }

    /**
     * Set presenter
     * @param presenter instance presenter for view
     */
    public void setPresenter(EditGroupDialogPresenter presenter) {
        this.presenter = presenter;
    }
}
