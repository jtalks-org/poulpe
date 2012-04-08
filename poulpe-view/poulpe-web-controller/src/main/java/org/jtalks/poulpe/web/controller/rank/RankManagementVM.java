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
import org.jtalks.common.validation.EntityValidator;
import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.service.RankService;
import org.jtalks.poulpe.validator.ValidationFailure;
import org.jtalks.poulpe.validator.ValidationFailureHandler;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

/**
 * ViewModel for rank management page.
 *
 * @author Pavel Vervenko
 */
public class RankManagementVM implements DialogManager.Performable, ValidationFailure {

    public static final String EDIT_RANK_ZUL = "/WEB-INF/pages/edit_rank.zul";
    public static final String EDIT_RANK_DIALOG = "#editRankDialog";
    public static final String CREATOR_TITLE = "ranks.edit.creator.title";
    public static final String MODIFIER_TITLE = "ranks.edit.modifier.title";
    public static final String DELETE_BUTTON_ID = "deleteButton";
    private ListModelList<Rank> items;
    private Rank selected;
    private Rank lastSelected;
    private RankService rankService;
    private EntityValidator entityValidator;
    private final DialogManager dialogManager;
    private ZkHelper zkHelper;

    private ValidationFailureHandler handler;

    /**
     * Construct the object with injected service.
     *
     * @param rankService     rankService to manipulate with ranks objects
     * @param entityValidator for validation
     * @param dialogManager   used to show confirmation
     */
    public RankManagementVM(@Nonnull RankService rankService,
                            EntityValidator entityValidator, DialogManager dialogManager) {
        this.rankService = rankService;
        this.entityValidator = entityValidator;
        this.dialogManager = dialogManager;
        initData();
    }

    /**
     * Wires ranks window to this ViewModel.
     *
     * @param component ranks window
     */
    @Init
    public void init(@ContextParam(ContextType.VIEW) Component component) {
        zkHelper = new ZkHelper(component);
        zkHelper.wireComponents(component, this);
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
     *
     * @return current selected item
     */
    public Rank getSelected() {
        enableOrDisableDeleteButton();
        return selected;
    }

    /**
     * Set selected Rank.
     *
     * @param selected selected rank
     */
    public void setSelected(Rank selected) {
        this.selected = selected;
    }

    /**
     * Command for creating new Rank.
     */
    @Command
    public void newItem() {
        lastSelected = selected;
        selected = new Rank("", 100);
        openEditorDialog(CREATOR_TITLE);
    }

    /**
     * Open the editor window.
     */
    @Command
    public void edit() {
        lastSelected = selected;
        openEditorDialog(MODIFIER_TITLE);
    }

    /**
     * Validate and save rank.
     */
    @Command
    @NotifyChange({"items", "selected"})
    public void save() {
        if (validate(selected)) {
            rankService.saveRank(selected);
            dialogClosed();
            initData();
        }
    }

    /**
     * Closes the rank dialog without saving and enable/disable delete button.
     */
    @Command
    @NotifyChange("selected")
    public void cancel() {
        dialogClosed();
        selected = lastSelected;
        enableOrDisableDeleteButton();
    }

    /**
     * Close the editor dialog
     */
    @Command
    public void dialogClosed() {
        Component dialog = zkHelper.findComponent(EDIT_RANK_DIALOG);
        dialog.detach();
    }

    @Override
    public void validationFailure(ValidationResult result) {
        handler = new ValidationFailureHandler("rankName", (Textbox) zkHelper.getCurrentComponent("rankName"));
        handler.validationFailure(result);
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
     * Execute Delete operation.
     */
    @Override
    @NotifyChange({"items", "selected"})
    public void execute() {
        for (Rank current : items.getSelection()) {
            rankService.deleteRank(current);
            items.remove(current);
        }
        selected = null;
    }

    private void enableOrDisableDeleteButton() {
        Button deleteButton = (Button) zkHelper.getCurrentComponent(DELETE_BUTTON_ID);
        if (selected == null) {
            deleteButton.setDisabled(true);
        } else {
            deleteButton.setDisabled(false);
        }
    }

    private void openEditorDialog(String title) {
        Window editorDialog = createEditRankDialog();
        editorDialog.setTitle(zkHelper.getLabel(title));
    }

    private Window createEditRankDialog() {
        return (Window) zkHelper.wireToZul(EDIT_RANK_ZUL);
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

    /**
     * Prepare list of String for confirmation window.
     *
     * @return names list of selected items
     */
    private List<String> getSelectionAsStringList() {
        List<String> list = new LinkedList<String>();
        for (Rank current : items.getSelection()) {
            list.add(current.getRankName());
        }
        return list;
    }

    /**
     * @param entityValidator the entityValidator to set
     */
    public void setEntityValidator(EntityValidator entityValidator) {
        this.entityValidator = entityValidator;
    }

    /**
     * Sets helper for zk operations.
     *
     * @param zkHelper the zkHelper to set
     */
    public void setZkHelper(ZkHelper zkHelper) {
        this.zkHelper = zkHelper;
    }

}
