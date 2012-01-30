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
package org.jtalks.poulpe.web.controller.section.moderation;

import java.util.List;

import org.jtalks.poulpe.model.entity.User;
import org.jtalks.poulpe.model.entity.Branch;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

@SuppressWarnings("serial")
public class ModerationDialogViewImpl extends Window implements ModerationDialogView, AfterCompose {

    private ModerationDialogPresenter presenter;

    private ListModelList<User> modelUsers;

    // COMPONENTS
    private Combobox userCombobox;
    private Listbox users;

    @Override
    public void afterCompose() {
        Components.wireVariables(this, this);
        Components.addForwards(this, this);

        userCombobox.setItemRenderer(userComboboxItemRenderer);
        users.setItemRenderer(userListItemRenderer);
    }

    private static ComboitemRenderer<User> userComboboxItemRenderer = new ComboitemRenderer<User>() {
        @Override
        public void render(Comboitem item, User user) throws Exception {
            item.setLabel(user.getUsername());
            item.setValue(user);
        }
    };
    
    private static ListitemRenderer<User> userListItemRenderer = new ListitemRenderer<User>() {
        @Override
        public void render(Listitem item, User user) throws Exception {
            item.appendChild(new Listcell(user.getUsername()));
            item.appendChild(new Listcell(user.getEmail()));
            item.appendChild(new Listcell("NOT IMPLEMENTED YET"));
            item.appendChild(new Listcell("NOT IMPLEMENTED YET"));
        }
    };
    
    /**
     * This event cause show dialog
     * 
     * @param event information about event contain Section which will be
     * deleted
     * */
    public void onOpen(Event event) {
        Branch branch = (Branch) event.getData();
        presenter.setBranch(branch);
        presenter.initView(this);
        showDialog();
    }

    public void onClose(Event event) {
        hideDialog();
    }

    @Override
    public void showDialog() {
        setVisible(true);
    }

    @Override
    public void hideDialog() {
        setVisible(false);
    }

    @Override
    public void updateView(List<User> users, List<User> usersInCombo) {
        this.users.setModel(new ListModelList<User>(users));
        userCombobox.setModel(new ListModelList<User>(usersInCombo));
    }

    public void setUserCombobox(Combobox combo) {
        this.userCombobox = combo;
    }

    public void setUsers(Listbox list) {
        this.users = list;
    }

    public void refreshView() {
        modelUsers.clear();
        presenter.refreshView();
    }

    public void onClick$confirmButton() {
        presenter.confirm();
    }

    public void onClick$rejectButton() {
        presenter.reject();
    }

    public void onClick$deleteButton() {
        if (users.getSelectedIndex() < 0) {
            return;
        }

        presenter.deleteUser(currentlySelectedUser());
    }

    private User currentlySelectedUser() {
        return (User) users.getModel().getElementAt(users.getSelectedIndex());
    }

    public void onClick$addButton() {
        Comboitem selectedItem = userCombobox.getSelectedItem();
        if (selectedItem != null) {
            User user = (User) selectedItem.getValue();
            presenter.addUser(user);
        }
    }

    @Override
    public void showComboboxErrorMessage(String message) {
        userCombobox.setErrorMessage(Labels.getLabel(message));
    }
    
    public void setPresenter(ModerationDialogPresenter presenter) {
        this.presenter = presenter;
    }

}
