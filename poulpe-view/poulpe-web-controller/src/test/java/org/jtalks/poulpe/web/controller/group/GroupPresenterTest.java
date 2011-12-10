package org.jtalks.poulpe.web.controller.group;

import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

public class GroupPresenterTest {
    
    private GroupPresenter presenter;
    
    @Mock
    private DialogManager dialogManager;
    @Mock
    private GroupService service;
    
    @Mock
    private GroupView view;
    
    
  @BeforeMethod
  public void beforeMethod() {
      MockitoAnnotations.initMocks(this);
      presenter = new GroupPresenter();
      presenter.setDialogManager(dialogManager);
      presenter.setGroupService(service);
  }

//
//  @Test
//  public void deleteGroup() {
//     presenter.initView(view);
//  }
//
//  @Test
//  public void doSearch() {
//    throw new RuntimeException("Test not implemented");
//  }
//
//  @Test
//  public void initView() {
//    throw new RuntimeException("Test not implemented");
//  }
//
//  @Test
//  public void onAddGroup() {
//    throw new RuntimeException("Test not implemented");
//  }
//
//  @Test
//  public void onEditGroup() {
//    throw new RuntimeException("Test not implemented");
//  }
//
//  @Test
//  public void setDialogManager() {
//    throw new RuntimeException("Test not implemented");
//  }
//
//  @Test
//  public void setGroupService() {
//    throw new RuntimeException("Test not implemented");
//  }
//
//  @Test
//  public void updateViewString() {
//    throw new RuntimeException("Test not implemented");
//  }
//
//  @Test
//  public void updateView() {
//    throw new RuntimeException("Test not implemented");
//  }
}
