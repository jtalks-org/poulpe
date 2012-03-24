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
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * View Model for rank management page.
 *
 * @author Pavel Vervenko
 */
public class RankManagementVM implements DialogManager.Performable, ValidationFailure {

    private ListModelList<Rank> items;
    private Rank selected;
    private RankService rankService;
    private EntityValidator entityValidator;
    private final DialogManager dialogManager;
    @Wire
    private Window ranksWindow;
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
        selected = new Rank("", 100);
        openEditorDialog("ranks.edit.creator.title");
    }

    /**
     * Open the editor window.
     */
    @Command
    public void edit() {
        openEditorDialog("ranks.edit.modifier.title");
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

    @Command
    @NotifyChange("selected")
    public void cancel() {
        dialogClosed();
        enableOrDisableDeleteButton();
    }

    /**
     * Close the editor dialog
     */
    @Command
    public void dialogClosed() {
        Component dialog = zkHelper.findComponent("#editRankDialog");
        dialog.detach();
    }

    @Override
    public void validationFailure(ValidationResult result) {
        //  new ValidationFailureHandler("name", rankName)
        // TODO: add initialization - now it causes NPE because rankName is null
        handler = new ValidationFailureHandler("rankName", (Textbox) getCurrentComponent("rankName"));
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
        Button deleteButton = (Button) getCurrentComponent("deleteButton");
        if(selected == null) {
            deleteButton.setDisabled(true);
        } else {
            deleteButton.setDisabled(false);
        }
    }

    private void openEditorDialog(String title) {
        Window editorDialog = createEditRankDialog();
        editorDialog.setTitle(Labels.getLabel(title));
    }

    private Window createEditRankDialog() {
        return (Window) zkHelper.wireToZul("/WEB-INF/pages/edit_rank.zul");
    }

    /**
     * Find the component by id.
     *
     * @param id component's Id
     * @return found component or null
     */
    private Component getCurrentComponent(String id) {
        for (Component c : Executions.getCurrent().getDesktop().getComponents()) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
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

}
