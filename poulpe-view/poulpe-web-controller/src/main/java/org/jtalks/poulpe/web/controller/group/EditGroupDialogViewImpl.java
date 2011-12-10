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

import java.util.List;
import org.hibernate.id.IdentityGenerator.GetGeneratedKeysDelegate;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.web.controller.branch.BranchEditorPresenter;
import org.jtalks.poulpe.web.controller.branch.BranchEditorView;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
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
 * */
public class EditGroupDialogViewImpl extends Window implements
        EditGroupDialogView, AfterCompose {

    private static final long serialVersionUID = 7388638074018815713L;

    private EditGroupDialogPresenter presenter;
    private Textbox groupName;
    private Textbox groupDescription;
    private Button confirmButton;
    private Button rejectButton;
    private Combobox sectionList;

  
  

    /**
     * Set presenter
     * 
     * @see BranchEditorPresenter
     * @param presenter
     *            instance presenter for view
     * */
    public void setPresenter(EditGroupDialogPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void afterCompose() {
        Components.addForwards(this, this);
        Components.wireVariables(this, this);
        presenter.initView(this,null);
        groupName.setConstraint("no empty");     
    }

    /**
     * Handle event when user click on confirm button
     * */
    public void onClick$confirmButton() {
        presenter.saveOrUpdateGroup(groupName.getText(), groupDescription.getText());
        
        hide();
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
        groupName.clearErrorMessage();
    }

    /**
     * Handle event from main window for open add new branch dialog
     * */
    public void onOpenAddDialog() {
        show();
    }

    /**
     * Handle event from main window for open edit branch dialog
     * 
     * @param event
     *            information about event
     * */
    public void onOpenEditDialog(Event event) {
        show((Group) event.getData());
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void hide() {
        setVisible(false);      
        Events.postEvent(new Event("onHideDialog", getDesktop().getPage(
                "mainPage").getFellow("mainWindow")));
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void show() {
        presenter.initView(this,null);
        setTitle(Labels.getLabel("groups.newbranchedialog.title"));
        confirmButton.setLabel(Labels.getLabel("groups.button.add"));
        rejectButton.setLabel(Labels.getLabel("groups.button.cancel"));
        groupName.setRawValue("");
        groupDescription.setText("");        
        setVisible(true);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void show(Group group) {
        presenter.initView(this,group);
        setTitle(Labels.getLabel("groups.editdialog.title"));
        confirmButton.setLabel(Labels.getLabel("groups.button.edit"));
        rejectButton.setLabel(Labels.getLabel("groups.button.cancel"));
        groupName.setText(group.getName());
        groupDescription.setText(group.getDescription());
        setVisible(true);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void notUniqueGroupName() {
        throw new WrongValueException(groupName, Labels.getLabel("groups.validation.not_unique_group_name"));
    }
}
