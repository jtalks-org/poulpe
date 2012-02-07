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

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;

import org.jtalks.common.model.entity.Rank;
import org.jtalks.common.validation.EntityValidator;
import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.service.RankService;
import org.jtalks.poulpe.validator.ValidationFailure;
import org.jtalks.poulpe.validator.ValidationFailureHandler;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;

/**
 * View Model for rank management page.
 *
 * @author Pavel Vervenko
 */
public class RankManagementVM implements DialogManager.Performable, ValidationFailure {

    private ListModelList<Rank> items;
    private Rank selected;
    private Textbox rankName;
    private RankService rankService;
    private EntityValidator entityValidator;
    private final DialogManager dialogManager;
    private String title;
    
    private ValidationFailureHandler handler;

    /**
     * Construct the object with injected service.
     *
     * @param rankService rankService to manipulate with ranks objects
     * @param entityValidator for validation
     * @param dialogManager used to show confirmation
     */
    public RankManagementVM(@Nonnull RankService rankService,
            EntityValidator entityValidator, DialogManager dialogManager) {
        this.rankService = rankService;
        this.entityValidator = entityValidator;
        this.dialogManager = dialogManager;
        initData();
    }

    /**
     * Get ranks list.
     *
     * @return ranks list
     */
    public ListModelList<Rank> getItems() {
        return items;
    }

    /**
     * Load data to the list.
     */
    private void initData() {
        items = new ListModelList<Rank>(rankService.getAll());
    }

    /**
     * Get selected item.
     * @return current selected item
     */
    public Rank getSelected() {
        return selected;
    }

    /**
     * Set selected Rank.
     * @param selected selected rank
     */
    public void setSelected(Rank selected) {
        this.selected = selected;
    }

    /**
     * Open the editor window.
     */
    @Command
    public void edit() {
    	title = "Edit rank";
        openEditor();
    }

    /**
     * Close the editor dialog
     */
    @Command
    public void dialogClosed() {
        //TODO: add code to close dialog
        getCurrentWindow("RankEditorWindow").setVisible(false);
    }

    /**
     * Validate and save rank.
     */
    @Command
    @NotifyChange({"items", "selected"})
    public void save() {
        if (validate(selected)) {
            rankService.saveRank(selected);
        }
        dialogClosed();
        initData();
    }

    private boolean validate(Rank rank) {
        ValidationResult result = entityValidator.validate(rank);

        if (result.hasErrors()) {
            validationFailure(result);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void validationFailure(ValidationResult result) {
        //  new ValidationFailureHandler("name", rankName)
        // TODO: add initialization - now it causes NPE because rankName is null
        handler.validationFailure(result);
    }

    /**
     * Command for creating new Rank.
     */
    @Command
    public void newItem() {
        selected = new Rank("new", 100);
        title = "Create rank";
        openEditor();
    }

    /**
     * Delete selected ranks.
     */
    @NotifyChange({"items", "selected"})
    @Command
    public void delete() {
        dialogManager.confirmDeletion(getSelectionAsStringList(), this);
    }

    /**
     * Find the window by id.
     * @param id window Id
     * @return found component or null
     */
    private Component getCurrentWindow(String id) {
        for (Component c : Executions.getCurrent().getDesktop().getComponents()) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Prepare list of String for confirmation window.
     * @return names list of selected items
     */
    private List<String> getSelectionAsStringList() {
        List<String> list = new LinkedList<String>();
        for (Rank current : items.getSelection()) {
            list.add(current.getRankName());
        }
        return list;
    }

    private void openEditor() {
        Executions.getCurrent().createComponents("/RankEditor.zul", getCurrentWindow("rankManagementWindow"), null);
    }

    /**
     * Execute Delete operation.
     */
    @Override
    @NotifyChange({"items", "selected"})
    public void execute() {
        for (Rank current : items.getSelection()) {
            rankService.deleteRank(current);
            items.remove(current);
        }
    }
    
    /**
     * @param entityValidator the entityValidator to set
     */
    public void setEntityValidator(EntityValidator entityValidator) {
        this.entityValidator = entityValidator;
    }

    /**
     * Returns the title of the rank editor window.
     * @return the rank editor window title
     */
	public String getTitle() {
		return title;
	}

	/**
     * Sets the title of the rank editor window.
     * @param title the title to set
     */
	public void setTitle(String title) {
		this.title = title;
	}

}
