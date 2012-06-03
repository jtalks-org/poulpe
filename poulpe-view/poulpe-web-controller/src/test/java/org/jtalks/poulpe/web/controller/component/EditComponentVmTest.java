package org.jtalks.poulpe.web.controller.component;

import org.jtalks.common.model.entity.Component;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertFalse;

/**
 *@author Kazancev Leonid
 */
public class EditComponentVmTest {
    @Mock
    private ComponentService componentService;

    private SelectedEntity<Component> selectedEntity = new SelectedEntity<Component>();

    @InjectMocks
    private EditComponentVm viewModel;

    @BeforeMethod
    public void setUp(){
        viewModel = spy(new EditComponentVm(componentService));
        MockitoAnnotations.initMocks(this);
        viewModel.setSelectedEntity(selectedEntity);
        selectedEntity.setEntity(new Jcommune());
    }

    @Test
    public void initData(){
        viewModel.initData();
        assertFalse(viewModel.getCurrentComponent() == null);
    }




}
