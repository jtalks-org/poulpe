package org.jtalks.poulpe.web.controller.ranks;

import static org.jtalks.poulpe.web.controller.rank.RankManagementVM.CREATOR_TITLE;
import static org.jtalks.poulpe.web.controller.rank.RankManagementVM.DELETE_BUTTON_ID;
import static org.jtalks.poulpe.web.controller.rank.RankManagementVM.EDIT_RANK_DIALOG;
import static org.jtalks.poulpe.web.controller.rank.RankManagementVM.EDIT_RANK_ZUL;
import static org.jtalks.poulpe.web.controller.rank.RankManagementVM.MODIFIER_TITLE;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.jtalks.common.model.entity.Rank;
import org.jtalks.common.validation.EntityValidator;
import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.service.RankService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.jtalks.poulpe.web.controller.rank.RankManagementVM;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

public class RanksManagementVmTest {
    private static final String CREATOR_TITLE_LABEL = "ranks.edit.creator.title.label";
    private static final String MODIFIER_TITLE_LABEL = "ranks.edit.modifier.title.label";
    RankManagementVM vm;
    @Mock
    RankService service;
    @Mock
    DialogManager dialogManager;
    @Mock
    ZkHelper zkHelper;
    @Mock 
    EntityValidator entityValidator;
    @Mock
    Window rankDialog;
    @Mock
    Button button;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        vm = new RankManagementVM(service, entityValidator, dialogManager);
        vm.setZkHelper(zkHelper);
    }

    @Test
    public void testNewItem() {
        when(zkHelper.wireToZul(anyString())).thenReturn(rankDialog);
        when(zkHelper.getLabel(CREATOR_TITLE)).thenReturn(CREATOR_TITLE_LABEL);
        
        vm.newItem();
        
        verify(zkHelper).wireToZul(EDIT_RANK_ZUL);
        verify(rankDialog).setTitle(CREATOR_TITLE_LABEL);
    }

    @Test
    public void testEdit() {
        when(zkHelper.wireToZul(anyString())).thenReturn(rankDialog);
        when(zkHelper.getLabel(MODIFIER_TITLE)).thenReturn(MODIFIER_TITLE_LABEL);
        
        vm.edit();
        
        verify(zkHelper).wireToZul(EDIT_RANK_ZUL);
        verify(rankDialog).setTitle(MODIFIER_TITLE_LABEL);
    }

    @Test
    public void testSave() throws Exception {
        when(zkHelper.findComponent(EDIT_RANK_DIALOG)).thenReturn(rankDialog);
        when(entityValidator.validate(any(Rank.class))).thenReturn(ValidationResult.EMPTY);
        Rank rank = new Rank("rank");
        vm.setSelected(rank);

        vm.save();

        verify(service).saveRank(rank);
        verify(rankDialog).detach();
        verify(service, times(2)).getAll();
    }

    @Test
    public void testDelete() {
        vm.delete();
        
        verify(dialogManager).confirmDeletion(Collections.<String> emptyList(), vm);
    }

    @Test
    public void testDialogClosed() {
        when(zkHelper.findComponent(EDIT_RANK_DIALOG)).thenReturn(rankDialog);
        
        vm.dialogClosed();
        
        verify(rankDialog).detach();
    }

    @Test
    public void testCancel() {
        when(zkHelper.findComponent(EDIT_RANK_DIALOG)).thenReturn(rankDialog);
        when(zkHelper.getCurrentComponent(DELETE_BUTTON_ID)).thenReturn(button);
        
        vm.cancel();
        
        verify(rankDialog).detach();
        verify(button).setDisabled(true);
        
    }
}
