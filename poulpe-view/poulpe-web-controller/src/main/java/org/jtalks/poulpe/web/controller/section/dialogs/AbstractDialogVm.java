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

/**
 * Since all the dialogs have {@code showDialog} property, it may worth using this abstract VM to have methods to show
 * dialogs out of box (of course reading of those methods should be ensured by user of the code).
 *
 * @author stanislav bashkirtsev
 */
public class AbstractDialogVm {
    protected static final String SHOW_DIALOG = "showDialog";
    private boolean showDialog = false;

    /**
     * Sets the flag of showing the dialog to {@code true}, is used by ZUL.
     *
     * @return this
     */
    protected AbstractDialogVm showDialog() {
        showDialog = true;
        return this;
    }

    /**
     * Decides whether to show the dialog or not (is used by ZUL). Changes to flag to {@code false} each time it's
     * invoked.
     *
     * @return {@code true} if the confirmation dialog should be shown, {@code false} otherwise
     */
    public boolean isShowDialog() {
        boolean tempResult = showDialog;
        showDialog = false;
        return tempResult;
    }
}
