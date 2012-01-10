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

import java.util.Map;

import javax.annotation.Nonnull;
import org.jtalks.poulpe.model.entity.Rank;
import org.jtalks.poulpe.service.RankService;
import org.jtalks.poulpe.validation.EntityValidator;
import org.jtalks.poulpe.validation.ValidationError;
import org.jtalks.poulpe.validation.ValidationResult;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.*;
import org.zkoss.zul.impl.InputElement;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.jtalks.poulpe.web.controller.DialogManager;

/**
 * View Model for rank management page.
 *
 * @author Pavel Vervenko
 */
public class RankManagementVM implements DialogManager.Performable {

    ListModelList<Rank> items;
    private RankService rankService;
    private Rank selected;
    private Component editor;
    private EntityValidator entityValidator;
    private Textbox rankName;
    private final DialogManager dialogManager;

    /**
     * Construct the object with injected service.
     *
     * @param rankService rankService
     */
    public RankManagementVM(@Nonnull RankService rankService, 
            EntityValidator entityValidator, DialogManager dialogManager) {
        this.rankService = rankService;
        this.entityValidator = entityValidator;
        this.dialogManager = dialogManager;
        initData();
    }

    /**
     * @param entityValidator the entityValidator to set
     */
    public void setEntityValidator(EntityValidator entityValidator) {
        this.entityValidator = entityValidator;
    }
    /**
     * Get ranks list.
     *
     * @return ranks list
     */
    @NotifyChange
    public ListModelList<Rank> getItems() {
        return items;
    }

    /**
     * Load data to the list.
     */
    private void initData() {
        items = new ListModelList<Rank>(rankService.getAll());
    }

    @NotifyChange
    public Rank getSelected() {
        return selected;
    }

    public void setSelected(Rank selected) {
        this.selected = selected;
    }

    @Command
    public void edit() {
        openEditor();
    }

    @Command
    public void check() {
    }

    @Command
    public void dialogClosed() {
        editor = null;
    }

    @Command
    @NotifyChange
    public void save()  {
        if (validate (selected)) {
            rankService.saveRank(selected);
        }
        getCurrentWindow("RankEditorWindow").setVisible(false);
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
    
    public void validationFailure(ValidationResult result) {
        Map<String, ? extends InputElement> comps = ImmutableMap.of("name",rankName);
        
        for (ValidationError error : result.getErrors()) {
            String fieldName = error.getFieldName();
            if (comps.containsKey(fieldName)) {
                String message = Labels.getLabel(error.getErrorMessageCode());

                InputElement ie = comps.get(fieldName);
                ie.setErrorMessage(message);
            }
        }
    }

    @Command
    public void newItem() {
        selected = new Rank("new", 100);
        openEditor();
    }

    @Command
    public void delete() {
        dialogManager.confirmDeletion(getSelectionAsStringList(), this);
    }
    
    private Component getCurrentWindow(String id) {
        for (Component c : Executions.getCurrent().getDesktop().getComponents()) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    private List<String> getSelectionAsStringList() {
        List<String> list = new LinkedList<String>();
        for(Rank current: items.getSelection()) {
            list.add(current.getRankName());
        }
        return list;
    }
    
    private void openEditor() {
        editor = Executions.getCurrent().createComponents("/RankEditor.zul", getCurrentWindow("rankManagementWindow"), null);
    }

    @Override
    public void execute() {
        for(Rank current: items.getSelection()) {
            rankService.deleteRank(current);
        }
    }
}
