package org.jtalks.poulpe.web.controller.group;

import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;

public class EditGroupDialogPresenter {
    
    private EditGroupDialogView view;
    private Group group;
    private GroupService groupService;
    
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    public void initView(EditGroupDialogView view, Group group){
        this.view = view;
        this.group = group;        
    }
    
    public void saveOrUpdateGroup(String name, String description){
        if(group == null){
            group = new Group();
        }
        group.setName(name);
        group.setDescription(description);
        try{
            groupService.saveGroup(group);
        }catch(NotUniqueException e){
            view.notUniqueGroupName();
        }
    }
    
    public void editGroup(String name, String description){
        
    }

}
