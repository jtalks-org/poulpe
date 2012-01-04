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
package org.jtalks.poulpe.web.controller.component;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

public class EditCompViewModel {
	/**
	 * The title of the component
	 */
	private String title;
	/**
	 * The description of the component
	 */
	private String description;
	/**
	 * Web-form validation messages
	 */
	Map<String, String> validationMessages = 
		new HashMap<String,String>();//validation messages

	// getters & setters
	/**
	 * Returns the title for current component
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title for current component
	 * @param description
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the description for current component
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description for current component
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	// commands
	@Command()
	@NotifyChange({ "title", "description" })
	public void showHello() {
//		title = description;
		validationMessages.clear();
	}
	
	// validators and validation messages
    /**
     * Returns the map with validation messages
     * @return map with validation messages
     */
	public Map<String, String> getValidationMessages() {
		return validationMessages;
	}
	
    /**
     * Returns title validator
     * @return title validator
     */
	public Validator getTitleValidator() {
		return new Validator() {
			public void validate(ValidationContext ctx) {
				String price = (String) ctx.getProperty().getValue();
				if (price == null || price.equals("")) {
					ctx.setInvalid(); // mark invalid
					validationMessages.put("title", "should not be empty");
				} else {
					validationMessages.remove("title");
				}
				// notify messages was changed.
				ctx.getBindContext().getBinder()
						.notifyChange(validationMessages, "title");
			}
		};
	}
}
