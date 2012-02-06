package org.jtalks.common.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.validation.annotations.UniqueConstraint;
import org.jtalks.common.validation.annotations.UniqueField;

/**
 * Forum section that contains branches.
 * 
 * @author Tatiana Birina
 * @author Guram Savinov
 */
@UniqueConstraint
public class Section extends Entity {
    /**
     * Error message if section already exist
     */
    public static final String SECTION_ALREADY_EXISTS = "sections.error.section_name_already_exists";
    /**
     * Error message if section can be void
     */
    public static final String SECTION_CANT_BE_VOID = "sections.error.section_name_cant_be_void";
    /**
     * Error message if section name is wrong
     */
    public static final String ERROR_LABEL_SECTION_NAME_WRONG = "sections.editsection.name.err";

    @UniqueField(message = SECTION_ALREADY_EXISTS)
    @NotNull(message = SECTION_CANT_BE_VOID)
    @NotEmpty(message = SECTION_CANT_BE_VOID)
    @Length(min = 1, max = 254, message = ERROR_LABEL_SECTION_NAME_WRONG)
    private String name;
    
    private String description;
    private Integer position;
    private List<Branch> branches = new ArrayList<Branch>();

    /**
     * Default constructor, protected for using only by Hibernate and subclasses
     */
    protected Section() {
    }

    /**
     * Creates a section with empty list of branches setting section a name
     * 
     * @param name for new section
     */
    public Section(String name) {
        this.name = name;
    }

    /**
     * Constructor with name and description, creates a section with empty list of branches
     * 
     * @param name - name for new section
     * @param description - description for new section
     */
    public Section(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Set section name which briefly describes the topics contained in it.
     * 
     * @return name section.
     */
    public String getName() {
        return name;
    }

    /**
     * Set section name.
     * 
     * @param name - name for section.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get section description.
     * 
     * @return description for section
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set section description which contains additional information about the
     * section.
     * 
     * @param description - description for section
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get section position.
     * 
     * @return position
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * Set section position.
     * 
     * @param position - position for section
     */
    public void setPosition(Integer position) {
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
     * @param branches - list of branches
     */
    protected void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    /**
     * Adds branch to the section or updates if it exist.
     * 
     * @param branch the branch for adding to section
     */
    public void addOrUpdateBranch(Branch branch) {
        for (int index = 0; index < branches.size(); index++) {
            long id = branches.get(index).getId();
            
            if (id != 0 && id == branch.getId()) {
                branches.set(index, branch);
                return;
            }
        }
        
        branches.add(branch);
    }

    /**
     * Delete branch from the section.
     * 
     * @param branch the branch for deleting from section
     */
    public void deleteBranch(Branch branch) {
        branches.remove(branch);
    }

    @Override
    public String toString() {
        return "PoulpeSection [id=" + getId() + ", name=" + name + ", description=" + description + "]";
    }


}