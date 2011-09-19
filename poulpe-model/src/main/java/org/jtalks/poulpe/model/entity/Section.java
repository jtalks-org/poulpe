/**
 * Copyright (C) 2011  jtalks.org Team
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
 * Also add information on how to contact you by electronic and paper mail.
 * Creation date: Apr 12, 2011 / 8:05:19 PM
 * The jtalks.org Project
 */
package org.jtalks.poulpe.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.jtalks.common.model.entity.Entity;

/**
 * forum section
 * 
 * @author tanya birina
 * 
 */
public class Section extends Entity {

    private String name;
    private Integer position;
    private String description;
    private List<Branch> branches;

    /**
     * Set section name which briefly describes the topics contained in it.
     * 
     * @return section name
     */
    public String getName() {
        return name;
    }

    /**
     * Set section name.
     *
     * @param name section name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get section description.
     * 
     * @return section description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set section description which contains additional information about the
     * section.
     * 
     * @param description
     *            section description
     */
    public void setDescription(String description) {
        this.description = description;
    } 
    
    /*
     * Get section position.
     * 
     * @return section position
     */
    public Integer getPosition () {
    	return position;
    }
    
    /*
     * Set section position.
     * 
     * @param position section position
     */
    public void setPosition (Integer position) {
    	this.position = position;
    }

    /**
     * Get section branches
     * 
     * @return list of branches
     */
    public List<Branch> getBranches() {
        return branches;
    }

    /**
     * Set section branches
     * 
     * @param branches set  list of branches
     */
    public void setBranches(List<Branch> branches) {
        if (branches == null) {
            branches = new ArrayList<Branch>();
        }
        this.branches = branches;
    }

    /**
     * Add branch to the section.
     *
     * @param branch branch
     */
    public void addBranch(Branch branch) {
        if (branches == null) {
            branches = new ArrayList<Branch>();
        }
        branches.add(branch);
    }

    /**
     * Delete branch from the section.
     *
     * @param branch branch
     */
    public void deleteBranch(Branch branch) {
        branches.remove(branch);
    }

}
