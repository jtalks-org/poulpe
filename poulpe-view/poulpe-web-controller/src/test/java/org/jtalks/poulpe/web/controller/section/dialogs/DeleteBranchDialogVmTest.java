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
package org.jtalks.poulpe.web.controller.section.dialogs;

import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.service.ForumStructureService;
import org.jtalks.poulpe.service.exceptions.JcommuneRespondedWithErrorException;
import org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguredException;
import org.jtalks.poulpe.service.exceptions.NoConnectionToJcommuneException;
import org.jtalks.poulpe.web.controller.section.ForumStructureItem;
import org.jtalks.poulpe.web.controller.section.ForumStructureVm;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author stanislav bashkirtsev
 */
public class DeleteBranchDialogVmTest {
    private  DeleteBranchDialogVm sut;
    private  ForumStructureService forumStructureService;
    private  ForumStructureVm forumStructureVm;

    @BeforeMethod
    public void beforeMethod(){
        forumStructureService = mock(ForumStructureService.class);
        forumStructureVm = mock(ForumStructureVm.class);
        sut = spy(new DeleteBranchDialogVm(forumStructureVm,forumStructureService));
    }

    @Test
    public void testIsShowDialog() throws Exception {
        sut.showDialog();
        assertTrue(sut.isShowDialog());
        assertFalse(sut.isShowDialog());
    }

    @Test
    public void testDeleteBranch(){
        sut.deleteBranch();
    }

    @Test
    public void testConfirmDeleteBranchWithContentNoConnection()
        throws NoConnectionToJcommuneException,JcommuneRespondedWithErrorException,JcommuneUrlNotConfiguredException{
        testConfirmDeleteBranchWithContent("branches.error.jcommune_no_connection", new NoConnectionToJcommuneException());
    }

    @Test
    public void testConfirmDeleteBranchWithContentJcommuneResponded()
            throws NoConnectionToJcommuneException,JcommuneRespondedWithErrorException,JcommuneUrlNotConfiguredException{
        testConfirmDeleteBranchWithContent("branches.error.jcommune_no_response", new JcommuneRespondedWithErrorException());
    }

    @Test
    public void testConfirmDeleteBranchWithContentJcommuneUrl()
            throws NoConnectionToJcommuneException,JcommuneRespondedWithErrorException,JcommuneUrlNotConfiguredException{
        testConfirmDeleteBranchWithContent("branches.error.jcommune_no_url", new JcommuneUrlNotConfiguredException());
    }

    @Test
    public void testConfirmDeleteBranchWithContentNoException()
            throws NoConnectionToJcommuneException,JcommuneRespondedWithErrorException,JcommuneUrlNotConfiguredException{
        testConfirmDeleteBranchWithContent(null,null);
    }

    private void testConfirmDeleteBranchWithContent(String message, Exception exception)
            throws NoConnectionToJcommuneException,JcommuneRespondedWithErrorException,JcommuneUrlNotConfiguredException{
        PoulpeBranch branch = new PoulpeBranch();
        ForumStructureItem forumStructureItem = new ForumStructureItem(branch);
        when(forumStructureVm.getSelectedItemInTree()).thenReturn(forumStructureItem);
        if(exception!=null){
            doNothing().when(sut).showError(message);
            doThrow(exception).when(forumStructureService).removeBranch(branch);
        }else{
            doNothing().when(forumStructureService).removeBranch(branch);
            doNothing().when(forumStructureVm).removeBranchFromTree(branch);
        }
        sut.confirmDeleteBranchWithContent();
    }

}
