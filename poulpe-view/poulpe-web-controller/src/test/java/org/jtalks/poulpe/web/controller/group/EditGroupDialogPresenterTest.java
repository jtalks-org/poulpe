package org.jtalks.poulpe.web.controller.group;

import org.testng.annotations.BeforeMethod;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.verification.*;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;
public class EditGroupDialogPresenterTest {
    private EditGroupDialogPresenter dialogPresenter;
    @Mock
    private GroupService mockGroupService;
    @Mock 
    private EditGroupDialogView mockView;
   @Mock
    private Group group;
    @BeforeMethod
    public void beforeMethod() {
        dialogPresenter = new EditGroupDialogPresenter();
        MockitoAnnotations.initMocks(this);

    }
    @Test
    public void testValidate(){
        final String nullName=null;
        final String normalName = "normal";
        final String longName = new String(new byte[255]);
        assertNotNull(dialogPresenter.validate(nullName));
        assertNull(dialogPresenter.validate(normalName));
        assertNotNull(dialogPresenter.validate(longName));
    }
    
    @Test
    public void testSaveOrUpdate(){
        final String name = "name";
        final String desc = "description";
        dialogPresenter.initView(mockView , group);
        dialogPresenter.setGroupService(mockGroupService);
        dialogPresenter.saveOrUpdateGroup(name,desc);
        try{
            verify(group).setName(name);
            verify(group).setDescription(desc);
            verify(mockGroupService).saveGroup(group);
        }catch (NotUniqueException ex){}        
    }
    @Test
    public void testSaveOrUpdateGroupWithException()throws NotUniqueException{
        Group nullGroup = new Group();   
     doThrow(new NotUniqueException()).when(mockGroupService).saveGroup(nullGroup);   
     final String name = "name";
     final String desc = "description";
     
     dialogPresenter.initView(mockView , nullGroup);
     dialogPresenter.setGroupService(mockGroupService);
     dialogPresenter.saveOrUpdateGroup(name,desc);
     verify(mockView).notUniqueGroupName();
    }
    
}
