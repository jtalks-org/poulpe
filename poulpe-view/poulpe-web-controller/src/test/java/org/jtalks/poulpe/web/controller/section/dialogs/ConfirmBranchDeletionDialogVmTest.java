package org.jtalks.poulpe.web.controller.section.dialogs;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author stanislav bashkirtsev
 */
public class ConfirmBranchDeletionDialogVmTest {
    ConfirmBranchDeletionDialogVm sut = new ConfirmBranchDeletionDialogVm();

    @Test
    public void testIsShowDialog() throws Exception {
        sut.showDialog();
        assertTrue(sut.isShowDialog());
        assertFalse(sut.isShowDialog());
    }
}
