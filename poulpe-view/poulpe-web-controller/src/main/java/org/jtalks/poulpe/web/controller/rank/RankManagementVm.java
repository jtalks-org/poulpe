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
package org.jtalks.poulpe.web.controller.rank;

import org.jtalks.common.model.entity.Rank;
import org.jtalks.poulpe.service.RankService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;

/**
 * ViewModel for rank management page.
 *
 * @author Pavel Vervenko
 * @author Ivaylo Kasaivanov
 */
public class RankManagementVm {

    private Rank selected;
    private ListModelList<Rank> ranks;
    private RankService rankService;
    private boolean showDialog;

    /**
     * Constructor for rank management view-model. Takes a rank service as an argument
     *
     * @param rankService rank service for operating with ranks
     */
    public RankManagementVm(RankService rankService) {
        this.rankService = rankService;
    }

    // -----===== Dialog Initiation =====-----

    /**
     * Opens a dialog, where a new rank can be created
     */
    @Command
    @NotifyChange({"selected", "showDialog"})
    public void openAddNewRankDialog() {
        selected = new Rank("", 100, true);
        showDialog = true;
    }


    /**
     * Opens a dialog, where currently selected rank could be edited
     */
    @Command
    @NotifyChange({"showDialog"})
    public void openEditRankDialog() {
        showDialog = true;
    }

    // -----===== Dialog Window Commands =====-----

    /**
     * Deletes the selected rank.
     */
    @Command
    @NotifyChange({"selected", "ranks"})
    public void deleteRank() {
        rankService.deleteRank(selected);
        ranks.remove(selected);
        selected = null;
    }

    /**
     * Save changes to an existing rank or adds a new one.This will close the dialog and bring us back to
     * the main window
     */

    @Command
    @NotifyChange({"selected", "showDialog"})
    public void saveRank() {
        rankService.saveRank(selected);
        if (!ranks.contains(selected)) {
            ranks.add(selected);
        }
        // Hide the dialog
        showDialog = false;
    }

    /**
     * Canceling any changes that has took place in the add/edit dialog. This will close the dialog and bring us back to
     * the main window
     */
    @Command
    @NotifyChange({"selected", "showDialog"})
    public void cancel() {
        selected = null;
        showDialog = false;
    }

    // ----===== Getters and Setters =====-----

    /**
     * Gets the rank that is currently selected
     *
     * @return returns selected rank
     */
    public Rank getSelected() {
        return selected;
    }

    /**
     * Sets the selected rank
     *
     * @param selected the rank that to be selected
     */
    public void setSelected(Rank selected) {
        this.selected = selected;
    }

    /**
     * Gets all existing ranks
     *
     * @return returns a model list of all ranks designated for usage by the associated Listbox
     */
    public ListModelList<Rank> getRanks() {
        if (ranks == null) {
            ranks = new ListModelList<Rank>(rankService.getAll());
        }
        return ranks;
    }


    /**
     * Check whether the add/edit dialog is visible
     *
     * @return true if the dialogs is visible, false otherwise
     */
    public boolean isShowDialog() {
        return showDialog;
    }


    /**
     * Get the rank service
     *
     * @return rank service to be used
     */
    public RankService getRankService() {
        return rankService;
    }
}
