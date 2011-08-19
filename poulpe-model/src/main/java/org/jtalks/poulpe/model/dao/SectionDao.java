package org.jtalks.poulpe.model.dao;

import java.util.List;

import org.jtalks.poulpe.model.entity.Section;

/*
 *  @author tanya birina
 */

public interface SectionDao extends Dao<Section> {
	 /**
     * Get the list of all sections.
     *
     * @return list of sections
     */
    List<Section> getAll();
    
    /**
     * Method to check if section name is already exists.
     * @param section
     * @return true if section with such name already exists
     */
    boolean isSectionNameExists(String section);

}
