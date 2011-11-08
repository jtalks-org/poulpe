package org.jtalks.poulpe.web.controller.section.moderation;

import java.util.ArrayList;
import java.util.List;

import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.web.controller.section.SectionPresenter;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

public class ModerationDialogViewImpl extends Window implements
        ModerationDialogView, AfterCompose {

    private ModerationDialogPresenter presenter;

    private ListModelList modelUsers;

    // COMPONENTS
    private Combobox userCombobox;
    private Listbox users;

    @Override
    public void afterCompose() {
        Components.wireVariables(this, this);
        Components.addForwards(this, this);
        userCombobox.setItemRenderer(new ComboitemRenderer() {

            @Override
            public void render(Comboitem item, Object data) throws Exception {
                item.setLabel(((User) data).getUsername());
                item.setValue(data);
            }
        });
        users.setItemRenderer(new ListitemRenderer() {
            @Override
            public void render(Listitem item, Object data) throws Exception {
                User curUser = (User) data;
                Listcell cell_1 = new Listcell(curUser.getUsername());
                Listcell cell_2 = new Listcell(curUser.getEmail());
                Listcell cell_3 = new Listcell("NOT IMPLEMENTED YET");
                Listcell cell_4 = new Listcell("NOT IMPLEMENTED YET");
                item.appendChild(cell_1);
                item.appendChild(cell_2);
                item.appendChild(cell_3);
                item.appendChild(cell_4);
            }
        });
        presenter.initView(this);
    }

    public void setPresenter(ModerationDialogPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * This event cause show dialog
     * 
     * @param event
     *            information about event contain Section which will be deleted
     * */
    public void onOpen(Event event) {
        Branch branch = (Branch) event.getData();
        presenter.setBranch(branch);
        showDialog(true);        
    }

    public void onClose(Event event) {
        showDialog(false);
    }

    public void showDialog(boolean showIt) {
        setVisible(showIt);
    }

    @Override
    public void updateView(List<User> users,List<User> usersInCombo) {        
        this.users.setModel(new ListModelList(users));        
        userCombobox.setModel(new ListModelList(usersInCombo));
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
    
    public void onClick$confirmButton(){
        presenter.onConfirm();
    }
    public void onClick$rejectButton(){
        presenter.onReject();
    }
    public void onClick$addButton(){        
        presenter.onAdd((User)userCombobox.getSelectedItem().getValue());
    }
}
