package org.jtalks.poulpe.service;

import java.util.List;

import org.jtalks.poulpe.service.exceptions.NotFoundException;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;
import org.jtalks.poulpe.model.entity.Section;

public interface SectionService extends EntityService<Section> {

	/**
     * Get list of all Sections.
     *
     * @return - list of Sections.
     */
    List<Section> getAll();
    
    /**
     * Mark the section as deleted.
     * @param section section to delete
     */
    boolean deleteSection(Section section);

    /**
     * Save or update section.
     * @param section instance to save
     * @throws NotUniqueException if section with the same name already exists
     */
    void saveSection(Section section) throws NotUniqueException;
    
    /**
     * Check if section with given name exists.
     * @param section
     * @return true if exists
     */
    boolean isSectionExists(String section);

}
