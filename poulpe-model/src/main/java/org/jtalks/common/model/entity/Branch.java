package org.jtalks.common.model.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.validation.annotations.UniqueConstraint;
import org.jtalks.common.validation.annotations.UniqueField;

/**
 * Forum branch that contains topics related to branch theme.
 * 
 * @author Pavel Vervenko
 */
@UniqueConstraint
public class Branch extends Entity {

    public static final String BRANCH_ALREADY_EXISTS = "branches.error.branch_name_already_exists";
    public static final String BRANCH_CANT_BE_VOID = "branches.error.branch_name_cant_be_void";
    public static final String ERROR_LABEL_SECTION_NAME_WRONG = "sections.editsection.name.err";

    @UniqueField(message = BRANCH_ALREADY_EXISTS)
    @NotNull(message = BRANCH_CANT_BE_VOID)
    @NotEmpty(message = BRANCH_CANT_BE_VOID)
    @Length(min = 1, max = 254, message = ERROR_LABEL_SECTION_NAME_WRONG)
    private String name;

    private String description;
    private Integer position;
    private Section section;

    /**
     * Default constructor, protected for using only by hibernate
     */
    protected Branch() {
    }

    /**
     * Create PoulpeBranch with name and description
     * 
     * @param name - name for new PoulpeBranch
     * @param description - description for new PoulpeBranch
     */
    public Branch(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Get branch name which briefly describes the topics contained in it.
     * 
     * @return PoulpeBranch name as String
     */
    public String getName() {
        return name;
    }

    /**
     * Set branch name.
     * 
     * @param name - PoulpeBranch name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get branch description.
     * 
     * @return PoulpeBranch description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set branch description which contains additional information about the
     * branch.
     * 
     * @param description - PoulpeBranch description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return its parent section
     */
    public Section getSection() {
        return section;
    }

    /**
     * Sets the section in which this branch is.
     * 
     * @param section the parent section
     */
    public void setSection(Section section) {
        this.section = section;
    }

    /**
     * Gets the position of this branch within all branches of its parent
     * section
     * 
     * @return the position
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * Sets the position of this branch within all branches of its parent
     * section
     * 
     * @param position the position to set
     */
    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "PoulpeBranch [id=" + getId() + ", name=" + name + ", description=" + description + ", position="
                + position + "]";
    }
}
