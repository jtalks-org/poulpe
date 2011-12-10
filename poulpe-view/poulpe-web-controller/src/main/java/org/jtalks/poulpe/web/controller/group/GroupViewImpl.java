package org.jtalks.poulpe.web.controller.group;

import java.util.List;

import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.web.controller.section.SectionView;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class GroupViewImpl extends Window implements GroupView, AfterCompose {

    private GroupPresenter presenter;

    private Window editDialog;

    private Listbox groupsListbox;
    private ListModelList groupsListboxModel;
    private Textbox searchTextbox;

    public void setPresenter(GroupPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void afterCompose() {
        Components.addForwards(this, this);
        Components.wireVariables(this, this);

        groupsListbox.setItemRenderer(new ListitemRenderer() {

            @Override
            public void render(Listitem arg0, Object arg1) throws Exception {
                final Group group = (Group) arg1;
                new Listcell(group.getName()).setParent(arg0);
                new Listcell("Not specified yet").setParent(arg0);
                arg0.setId(String.valueOf(group.getId()));
            }
        });
        presenter.initView(this);
    }

    @Override
    public void updateView(List<Group> groups) {
        groupsListboxModel = new ListModelList(groups);
        groupsListbox.setModel(groupsListboxModel);
    }

    public void onDoubleClick$groupsListbox() {
        presenter.onEditGroup(getSelectedGroup());
        
    }

    public void onClick$addButton() {
        presenter.onAddGroup();
    }
    
    public void onClick$removeButton() {
        presenter.deleteGroup(getSelectedGroup());
    }
    
    public void onSearchAction() {
        presenter.doSearch(searchTextbox.getText());
    }

    @Override
    public void openNewDialog() {
        Component component = getDesktop().getPage("GroupDialog").getFellow(
                "editWindow");
        component.setAttribute("presenter", presenter);
        Events.postEvent(new Event("onOpenAddDialog", component));
    }    
    
    @Override
    public void openEditDialog(Group group) {
        Events.postEvent(new Event("onOpenEditDialog", getDesktop().getPage(
                "GroupDialog").getFellow("editWindow"),group));
        
    }
    
    public void onHideDialog() {
        presenter.updateView();
    }

    @Override
    public void showConfirmDialog() {
        // TODO Auto-generated method stub

    }

    public Group getSelectedGroup() {
        return (Group) groupsListboxModel.getElementAt(groupsListbox
                .getSelectedIndex());
    }

}
