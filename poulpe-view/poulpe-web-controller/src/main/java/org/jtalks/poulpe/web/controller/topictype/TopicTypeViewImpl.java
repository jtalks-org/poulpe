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
package org.jtalks.poulpe.web.controller.topictype;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Pavel Vervenko
 * @author Vahluev Vyacheslav
 */
public class TopicTypeViewImpl extends Window implements TopicTypeView {

    /**
     * Generated uid
     */
    private static final long serialVersionUID = 1657959037954482623L;
    
    private Textbox titleTextbox;
    private Textbox descriptionTextbox;
    private Button editButton;
    private Button createButton;

    @Override
    public void showTypeTitle(String title) {
        titleTextbox.setText(title);
    }
    
    @Override
    public String getTypeTitle() {
        return titleTextbox.getText();
    }

    @Override
    public void showTypeDescription(String description) {
        descriptionTextbox.setText(description);
    }

    @Override
    public String getTypeDescription() {
        return descriptionTextbox.getText();
    }

    @Override
    public void hideEditAction() {
        editButton.setVisible(false);
    }

    @Override
    public void hideCreateAction() {
        createButton.setVisible(false);
    }

    @Override
    public void openErrorPopupInTopicTypeDialog(String label) {
        final String message = Labels.getLabel(label);
        titleTextbox.setErrorMessage(message);
    }
    
}
