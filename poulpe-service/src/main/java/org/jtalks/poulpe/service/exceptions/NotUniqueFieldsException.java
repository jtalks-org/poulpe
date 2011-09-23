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
package org.jtalks.poulpe.service.exceptions;

import java.util.Set;

import org.jtalks.poulpe.model.dao.DuplicatedField;

/**
 * The exception which is thrown when saving entity to the date source violates DB constraints.
 * It also holds a set of fields that herewith are duplicated.
 * @author Dmitriy Sukharev
 */
public class NotUniqueFieldsException extends Exception {

    private Set<DuplicatedField> duplicates;

    /**
     * The only constructor which stores duplicated fields.
     * @param set the set of duplicated fields
     */
    public NotUniqueFieldsException(Set<DuplicatedField> set) {
        duplicates = set;
    }

    public Set<DuplicatedField> getDuplicates() {
        return duplicates;
    }

    private static final long serialVersionUID = -7924104333502563898L;
}
