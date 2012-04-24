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
package org.jtalks.poulpe.web.controller;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;

/**
 * Converter which can be used for wrap some text with prefix and suffix. Prefix and suffix value can be keys to label
 * from locale-dependent file (resource bundle).
 * 
 * <h3>Example:</h3>
 * <ol>
 * <li>In view-model:<br />
 * <code>PrefixedTextConverter prefixedTextConverter = new PrefixedTextConverter();</code>
 * <li>Use in .zul:<br />
 * <code>@load(vm.sometext) @converter(vm.prefixedTextConverter, prefix='labels.some_label')</code>
 * <li>Define resource string with name '{@code labels.some_label}' in your locale-dependent file
 * </ol>
 * 
 * @see <a href="http://books.zkoss.org/wiki/ZK%20Developer%27s%20Reference/MVVM/Data%20Binding/Converter" >ZK
 * Developer's Reference /MVVM/Data Binding/Converter</a>
 * @see org.zkoss.util.resource.Labels
 * 
 * @author Vyacheslav Zhivaev
 */
public class PrefixedTextConverter implements Converter, Serializable {

    private static final long serialVersionUID = -535890929316253205L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Object coerceToBean(Object src, Component component, BindContext context) {
        if (src == null) {
            return null;
        }
        return wrap(src.toString(), (String) context.getConverterArg("prefix"),
                (String) context.getConverterArg("suffix"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object coerceToUi(Object src, Component component, BindContext context) {
        return coerceToBean(src, component, context);
    }

    /**
     * Wraps {@code src} value with prefix and suffix. Prefix and suffix value can be keys to label from
     * locale-dependent file.
     * 
     * @param src the text to wrap
     * @param prefix the prefix to be added to {@code src}
     * @param suffix the suffix to be added to {@code src}
     * @return wrapped string
     */
    private String wrap(String src, String prefix, String suffix) {
        return StringUtils.defaultString(Labels.getLabel(prefix, prefix)) + src
                + StringUtils.defaultString(Labels.getLabel(suffix, suffix));
    }
}
