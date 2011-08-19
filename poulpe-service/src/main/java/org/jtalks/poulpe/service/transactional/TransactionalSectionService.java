package org.jtalks.poulpe.service.transactional;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.model.dao.SectionDao;
import org.jtalks.poulpe.service.SectionService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;

/**
 * 
 * @author tanya birina
 *
 */
public class TransactionalSectionService extends
		AbstractTransactionalEntityService<Section, SectionDao> implements SectionService {
	/**
     * Create an instance of entity based service
     *
     * @param sectionDao - data access object
     */
    public TransactionalSectionService(SectionDao sectionDao) {
        this.dao = sectionDao;
    }
	
	 /**
     * {@inheritDoc}
     */
	@Override
	public boolean isSectionExists(String section) {
		return dao.isSectionNameExists (section);
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public List<Section> getAll() {
		return dao.getAll ();
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public boolean deleteSection(Section section) {
		 return dao.delete(section.getId());
		
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public void saveSection(Section section) throws NotUniqueException {
        try {
            dao.saveOrUpdate(section);
        } catch (ConstraintViolationException e) {
            throw new NotUniqueException(e);
        }
    }

}
