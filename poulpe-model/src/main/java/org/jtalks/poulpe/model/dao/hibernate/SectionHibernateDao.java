package org.jtalks.poulpe.model.dao.hibernate;

import java.util.List;

import org.jtalks.poulpe.model.dao.SectionDao;
import org.jtalks.poulpe.model.entity.Section;

public class SectionHibernateDao extends AbstractHibernateDao<Section> implements
		SectionDao {

	/**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Section> getAll() {
        return getSession().createQuery("from Section").list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSectionNameExists(String section) {
        return ((Number) getSession()
                .createQuery("select count(*) from Section s where s.name = ?")
                .setString(0, section)
                .uniqueResult()).intValue() != 0;
    }

}
