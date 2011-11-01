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
package org.jtalks.poulpe.web.controller.users;

import java.util.List;

import org.jtalks.common.model.entity.User;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * This class is implementation view for managing users
 * 
 * @author Vytautas Kashchuk
 */
public class UsersListViewImpl extends Window implements UsersListPresenter.UsersListView, AfterCompose {

private static final long serialVersionUID = 1L;
    
    private Listbox usersListbox;
    private UsersListPresenter presenter;

    private Textbox searchTextbox;
    
    @Override
	public void showSearchString(String searchString) {
		searchTextbox.setText(searchString);
	}

	@Override
	public String getSearchString() {
		return searchTextbox.getText();
	}

    public void setPresenter(UsersListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showUsersList(List<User> list) {
    	usersListbox.setModel(new ListModelList(list));
    }

    @Override
    public void afterCompose() {
        Components.wireVariables(this, this);
        Events.addEventListeners(this, presenter);
        initializeUsersListbox();
        presenter.initView(this);
    }

    private void initializeUsersListbox() {
        usersListbox.setItemRenderer(new ListitemRenderer() {
            @Override
            public void render(Listitem item, Object data) throws Exception {
                final User user = (User) data;
                item.setValue(user);
                new Listcell(user.getUsername()).setParent(item);
                new Listcell(user.getEmail()).setParent(item);
                new Listcell(user.getFirstName()).setParent(item);
                new Listcell(user.getLastName()).setParent(item);
                new Listcell(user.getRole()).setParent(item);   
                item.addEventListener(Events.ON_DOUBLE_CLICK, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        presenter.onEditAction(user);
                    }
                });
            }});
        usersListbox.setMultiple(false);
    }
}