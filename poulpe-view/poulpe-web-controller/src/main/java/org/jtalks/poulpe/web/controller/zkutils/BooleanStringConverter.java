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
package org.jtalks.poulpe.web.controller.zkutils;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zk.ui.Component;

/**
 * Class is usefull for example: to represent boolean parameter of object as checkbox, even if it is stored in object
 * as string.(For example: string-property email_notification_enabled(which can be only "true" ot "false") of jcommune's
 * component)
 *
 * @author Nickolay Polyarniy
 */
public class BooleanStringConverter implements Converter {

    /**
     * Convert String to Boolean
     * @param value string to be converted
     * @param comp associated component
     * @param ctx bind context for associate Binding and extra parameter (e.g. format)
     * @return the converted Boolean. For string.equalsIgnoreIgnoreCase("true") - it returns true(for example: if
     * string = "true", string = "TrUe"), else - false(even if string = "smth#$%", or string is null)
     */
    @Override
    public Object coerceToUi(Object value, Component comp, BindContext ctx) {
        String string = (String) value;
        return Boolean.parseBoolean(string);
    }

    /**
     * Convert Boolean to String
     * @param value boolean to be converted
     * @param comp associated component
     * @param ctx bind context for associate Binding and extra parameter (e.g. format)
     * @return the converted String. For boolean.equals(Boolean.TRUE) - it returns "true", else - "false"(even if
     * boolean-value is null)
     */
    @Override
    public Object coerceToBean(Object value, Component comp, BindContext ctx) {
        Boolean bool = (Boolean) value;
        return bool.toString();
    }
}
