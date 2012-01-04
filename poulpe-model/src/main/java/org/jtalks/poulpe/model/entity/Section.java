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
package org.jtalks.poulpe.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.validation.annotations.UniqueConstraint;
import org.jtalks.poulpe.validation.annotations.UniqueField;

/**
 * Forum section that contains branches. 
 * 
 * @author tanya birina
 * 
 */
@UniqueConstraint
public class Section extends Entity {
	public static final String SECTION_ALREADY_EXISTS = "sections.error.section_name_already_exists";
	public static final String SECTION_CANT_BE_VOID = "sections.error.section_name_cant_be_void";
	public static final String ERROR_LABEL_SECTION_NAME_WRONG = "sections.editsection.name.err";
	
	@UniqueField(message = SECTION_ALREADY_EXISTS)
    @NotNull(message = SECTION_CANT_BE_VOID)
    @NotEmpty(message = SECTION_CANT_BE_VOID)
    @Length(min = 1, max = 254, message = ERROR_LABEL_SECTION_NAME_WRONG)
    private String name;
    private String description;
    private Integer position;
    private List<Branch> branches = new ArrayList<Branch>();

    public Section() {
    }
    
    public Section(String name) {
        this.name = name;
    }

    public Section(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Set section name which briefly describes the topics contained in it.
     */
    public String getName() {
        return name;
    }

    /**
     * Set section name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get section description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set section description which contains additional information about the
     * section.
     */
    public void setDescription(String description) {
        this.description = description;
    } 
    
    /**
     * Get section position.
     */
    public Integer getPosition () {
        return position;
    }
    
    /**
     * Set section position.
     */
    public void setPosition (Integer position) {
        this.position = position;
    }

    /**
     * Get section branches
     */
    public List<Branch> getBranches() {
        return branches;
    }

    /**
     * Set section branches
     */
    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    /**
     * Add branch to the section.
     */
    public void addBranch(Branch branch) {
        branches.add(branch);
    }

    /**
     * Delete branch from the section.
     */
    public void deleteBranch(Branch branch) {
        branches.remove(branch);
    }

}
