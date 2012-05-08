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

import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.*;

import java.util.List;

/**
 * @author Konstantin Akimov
 * @author Vyacheslav Zhivaev
 * @author Kazancev Leonid
 */
@SuppressWarnings("serial")
public class ZkGroupView extends Window implements AfterCompose {

    private GroupPresenter presenter;

    private ListModelList<Group> groupsListboxModel;
    private ZkHelper zkHelper = new ZkHelper(this);

    private Window editDialog;
    private Button removeButton;
    private static final String POPUP_MENU="editPopupMenu";
    private Menuitem editMembersMenuitem;

    private Listbox groupsListbox;
    private Textbox searchTextbox;

    @Override
    public void afterCompose() {
        zkHelper.wireByConvention();

        groupsListbox.setItemRenderer(groupListRenderer);

        presenter.initView(this);
    }

    private static final ListitemRenderer<Group> groupListRenderer = new ListitemRenderer<Group>() {
        @Override
        public void render(Listitem listItem, Group group, int index) throws Exception {
            Listcell listcellName = new Listcell(group.getName());
            Listcell listcellUsersCount = new Listcell(Integer.toString(group.getUsers().size()));

            listcellName.setContext(POPUP_MENU);
            listcellUsersCount.setContext(POPUP_MENU);

            listItem.appendChild(listcellName);
            listItem.appendChild(listcellUsersCount);
            listItem.setId(String.valueOf(group.getId()));
        }
    };

    public void updateView(List<Group> groups) {
        groupsListboxModel = new ListModelList<Group>(groups);
        groupsListbox.setModel(groupsListboxModel);
        disableRemoveAndEditButtons();
    }

    public void onDoubleClick$groupsListbox() {
        if (correctSelection()) {
            presenter.onEditGroup(getSelectedGroup());
        }
    }

    public void onClick$addButton() {
        presenter.onAddGroup();
    }

    public void onSearchAction() {
        presenter.doSearch(searchTextbox.getText());
    }

    public void onClick$groupsListbox() {
        if (correctSelection()) {
            enableRemoveAndEditButtons();
        }
    }

    public void onClick$removeButton() {
        if (correctSelection()) {
            presenter.deleteGroup(getSelectedGroup());
        }
    }

    public void onClick$editMembersMenuitem() {
        if (correctSelection()) {
            presenter.editMembers(getSelectedGroup());
        }
    }

    public void openNewDialog() {
        EditGroupDialogView component = getEditView();
        component.show();
    }

    /**
     * Checking selected index to prevent list header selection.
     *
     * @return True when Selected index are correct.
     */
    private boolean correctSelection() {
        return groupsListbox.getSelectedIndex() != -1;
    }

    private EditGroupDialogView getEditView() {
        ZkEditGroupDialogView component = getEditDialogComponent();
        component.setAttribute("presenter", presenter);
        component.setAttribute("backWindow", this);
        return component;
    }

    private ZkEditGroupDialogView getEditDialogComponent() {
        return (ZkEditGroupDialogView) getDesktop().getPage("GroupDialog").getFellow("editWindow");
    }

    public void openEditDialog(Group group) {
        EditGroupDialogView component = getEditView();
        component.show(group);
    }

    public void onHideDialog() {
        presenter.updateView();
    }

    public Group getSelectedGroup() {
        return groupsListboxModel.getElementAt(groupsListbox.getSelectedIndex());
    }

    private void enableRemoveAndEditButtons() {
        removeButton.setDisabled(false);
    }

    private void disableRemoveAndEditButtons() {
        removeButton.setDisabled(true);
    }

    public void setPresenter(GroupPresenter presenter) {
        this.presenter = presenter;
    }

}
