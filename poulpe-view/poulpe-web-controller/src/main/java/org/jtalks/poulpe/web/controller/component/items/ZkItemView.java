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
package org.jtalks.poulpe.web.controller.component.items;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.validator.ValidationFailureHandler;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.jtalks.poulpe.web.controller.component.ListView;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * The class which manages actions and represents information about component
 * displayed in administrator panel.
 * 
 * @author Dmitriy Sukharev
 * @author Alexey Grigorev
 * @author Vyacheslav Zhivaev
 */
public class ZkItemView extends Window implements ItemView, AfterCompose {

    private static final long serialVersionUID = -3927090308078350369L;

    private transient ItemPresenter presenter;
    
    private Longbox cid;
    private Textbox name;
    private Textbox description;
    private Combobox componentType;

    private ValidationFailureHandler validationHandler;
    
    private ZkHelper zkHelper = new ZkHelper(this);

    /** {@inheritDoc} */
    @Override
    public void afterCompose() {
        zkHelper.wireByConvention();
        presenter.setView(this);
        validationHandler = new ValidationFailureHandler("name", name, "componentType", componentType);
    }
    
    @Override
    public void validationFailure(ValidationResult result) {
        validationHandler.validationFailure(result);
    }
    
    // ==== ItemView methods ====

    /** {@inheritDoc} */
    @Override
    public void show(Component component) {
        presenter.edit(component);
        showForm();
    }

    /** {@inheritDoc} */
    @Override
    public void showEmpty() {
        presenter.create();
        showForm();
    }

    private void showForm() {
        setVisible(true);
        name.setFocus(true);
        setValidationConstraints("");
    }

    /** {@inheritDoc} */
    @Override
    public void hide() {
        permitAnyInput();
        setVisible(false);
        ListView listView = (ListView) getAttribute("backWin");
        if (null != listView) {
            listView.updateList();
        }
    }

    private void permitAnyInput() {
        setValidationConstraints(null);
    }

    /**
     * Sets the constrains for name and component type fields.
     * @param constraints the constraints to be set
     */
    private void setValidationConstraints(String constraints) {
        componentType.setConstraint(constraints);
        name.setConstraint(constraints);
    }

    // ==== UI events ====

    /**
     * Performs validation and tells to presenter that user want to save created
     * or edited component in the component list.
     */
    public void onClick$saveCompButton() {
        setValidationConstraints("no empty");
        presenter.saveComponent();
    }

    /**
     * Hides the window for adding or editing component.
     */
    public void onClick$cancelButton() {
        hide();
    }

    /**
     * Tells to presenter to check if name of created or edited component is a
     * duplicate.
     * @see org.jtalks.poulpe.web.controller.component.ListPresenter
     */
    public void onBlur$name() {
        presenter.checkComponent();
    }
    
    /**
     * Handle event when name field in focus
     */
    public void onFocus$name() {
        name.clearErrorMessage();
    }

    // ==== ItemDataView methods ====

    /** {@inheritDoc} */
    @Override
    public long getComponentId() {
        return cid.getValue();
    }

    /** {@inheritDoc} */
    @Override
    public void setComponentId(long cid) {
        this.cid.setValue(cid);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return name.getText();
    }

    /** {@inheritDoc} */
    @Override
    public void setName(String compName) {
        this.name.setText(compName);
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription() {
        return description.getText();
    }

    /** {@inheritDoc} */
    @Override
    public void setDescription(String description) {
        this.description.setText(description);
    }

    /** {@inheritDoc} */
    @Override
    public ComponentType getComponentType() {
        String componentTypeValue = componentType.getText();
        if (StringUtils.isEmpty(componentTypeValue)) {
            return null;
        }
        
        return ComponentType.valueOf(componentTypeValue);
    }

    /** {@inheritDoc} */
    @Override
    public void setComponentType(ComponentType type) {
        if (type == null) {
            this.componentType.setText(null);
        } else {
            this.componentType.setText(type.toString());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setComponentTypes(Set<ComponentType> types) {
        componentType.getChildren().clear();
        componentType.appendItem(componentType.getText());
        for (ComponentType type : types) {
            componentType.appendItem(type.toString());
        }
    }

    // ==== getters and setters ====

    /**
     * @return presenter linked to this view
     */
    public ItemPresenter getPresenter() {
        return presenter;
    }

    /**
     * @param presenter to be linked with this view
     */
    public void setPresenter(ItemPresenter presenter) {
        this.presenter = presenter;
    }
    

}