/**
 * Copyright (C) 2011  jtalks.org Team
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
 * Also add information on how to contact you by electronic and paper mail.
 * Creation date: Apr 12, 2011 / 8:05:19 PM
 * The jtalks.org Project
 */
package org.jtalks.poulpe.web.controller.branch;

import org.jtalks.poulpe.model.entity.Branch;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * This class is implementation view for single branch
 * 
 * @author Bekrenev Dmitry
 * */
public class BranchDialogViewImpl extends Window implements BranchDialogView,
        AfterCompose {

    private static final long serialVersionUID = 7388638074018815713L;

    private BranchPresenter presenter;
    private Textbox branchName;
    private Textbox branchDescription;
    private Button confirmButton;
    private Button rejectButton;
    private Branch branch;

    /**
     * Set presenter
     * 
     * @see BranchEditorPresenter
     * @param presenter
     *            instance presenter for view
     * */
    public void setPresenter(BranchPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void afterCompose() {
        Components.addForwards(this, this);
        Components.wireVariables(this, this);
        presenter.setView(this);
        branchName.setConstraint("no empty");
    }

    /**
     * Handle event when user click on confirm button
     * */
    public void onClick$confirmButton() {
        presenter.saveBranch();
    }

    /**
     * Handle event when user click on reject button
     * */
    public void onClick$rejectButton() {
        hide();
    }

    /**
     * Handle event when branch name field in focus
     * */
    public void onFocus$branchName() {
        branchName.clearErrorMessage();
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public Branch getBranch() {
        branch.setName(branchName.getText().trim());
        branch.setDescription(branchDescription.getText().trim());
        return branch;
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void hide() {
        setVisible(false);
        Events.postEvent(new Event("onHideDialog", getDesktop().getPage(
                "branchEditor").getFellow("editorWindow")));
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void show() {
        setTitle(Labels.getLabel("branches.newbranchedialog.title"));
        confirmButton.setLabel(Labels.getLabel("branches.button.add"));
        rejectButton.setLabel(Labels.getLabel("branches.button.cancel"));
        branchName.setRawValue("");
        branchDescription.setText("");
        branch = new Branch();
        setVisible(true);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void show(Branch branch) {
        setTitle(Labels.getLabel("branches.editdialog.title"));
        confirmButton.setLabel(Labels.getLabel("branches.button.edit"));
        rejectButton.setLabel(Labels.getLabel("branches.button.cancel"));

        branchName.setText(branch.getName());
        branchDescription.setText(branch.getDescription());
        this.branch = branch;
        setVisible(true);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void notUniqueBranchName() throws WrongValueException {
        throw new WrongValueException(branchName, Labels.getLabel("branches.error.branch_name_already_exists"));
    }

}
