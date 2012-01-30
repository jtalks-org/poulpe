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
package org.jtalks.poulpe.web.controller.section;

import java.util.List;

import org.jtalks.poulpe.model.entity.Section;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Window;

/**
 * This class implementation for Delete section dialog
 * 
 * @author Bekrenev Dmitry
 * @author Alexey Grigorev
 */
public class DeleteSectionDialogViewImpl extends Window implements DeleteSectionDialogView, AfterCompose {

    private static final long serialVersionUID = -4999382692611273729L;
    
    private Radiogroup deleteMode;
    private Radio removeAndMoveMode;
    private Combobox selectedSection;
    
    private DeleteSectionDialogPresenter presenter;

    private Section deletedSection;

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterCompose() {
        Components.wireVariables(this, this);
        Components.addForwards(this, this);

        presenter.setView(this);
        selectedSection.setItemRenderer(itemRenderer);
    }
    
    private static ComboitemRenderer<Section> itemRenderer = new ComboitemRenderer<Section>() {
        @Override
        public void render(Comboitem item, Section section) {
            item.setLabel(section.getName());
            item.setDescription(section.getDescription());
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public Section getSectionToDelete() {
        return deletedSection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Section getRecipientSection() {
        return (Section) selectedSection.getModel().getElementAt(selectedSection.getSelectedIndex());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SectionDeleteMode getDeleteMode() {
        String mode = deleteMode.getItemAtIndex(deleteMode.getSelectedIndex()).getValue();
        return SectionDeleteMode.fromString(mode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showDialog(Section section) {
        deletedSection = section;
        showDialog();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void showDialog() {
        presenter.initView();
        setDefaultSection();
        deleteMode.setSelectedIndex(0);
        show();
    }

    private void show() {
        setVisible(true);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void closeDialog() {
        setVisible(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initSectionList(List<Section> selectableSections) {
        if (!selectableSections.isEmpty()) {
            initCombobox(selectableSections);
        } else {
            initDisabledCombobox();
        }
    }

    private void initCombobox(List<Section> selectableSections) {
        ListModelList<Section> modelList = new ListModelList<Section>(selectableSections);
        modelList.remove(deletedSection);
        
        selectedSection.setModel(modelList);
        selectedSection.setDisabled(false);
        selectedSection.setRawValue(null);

        removeAndMoveMode.setDisabled(false);
    }
    
    private void initDisabledCombobox() {
        selectedSection.setModel(null);
        selectedSection.setDisabled(true);
        removeAndMoveMode.setDisabled(true);
    }

    /**
     * Choice default section in combobox
     */
    private void setDefaultSection() {
        @SuppressWarnings("unchecked")
        ListModelList<Section> model = (ListModelList<Section>) selectedSection.getModel();
        model.clearSelection();

        if (!model.isEmpty()) {
            model.addSelection(model.get(0));
        } else {
            model.addSelection(null);
        }

        selectedSection.setModel(model);
    }

    /**
     * This event cause show dialog
     * 
     * @param event information about event contain Section which will be
     * deleted
     */
    public void onOpenDeleteSectionDialog(Event event) {
        showDialog((Section) event.getData());
    }

    /**
     * This event cause delete
     */
    public void onClick$confirmButton() {
        presenter.delete();
    }

    /**
     * This event cause close dialog
     */
    public void onClick$rejectButton() {
        closeDialog();
    }

    /**
     * @param presenter DeleteSectionDialogPresenter instance
     */
    public void setPresenter(DeleteSectionDialogPresenter presenter) {
        this.presenter = presenter;
    }
    
    public DeleteSectionDialogPresenter getPresenter() {
        return presenter;
    }
}
