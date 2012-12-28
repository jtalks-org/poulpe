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
package org.jtalks.poulpe.service;

import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.exceptions.JcommuneRespondedWithErrorException;
import org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguredException;
import org.jtalks.poulpe.service.exceptions.NoConnectionToJcommuneException;

public interface JCommuneNotifier {

    /**
     * Notifies delete the section
     *
     * @param jCommuneUrl JCommune URL
     * @param section which will be deleted
     * @throws NoConnectionToJcommuneException some connection problems happened, while trying to notify JCommune
     * @throws JcommuneRespondedWithErrorException occurs when the response status is not {@code OK 200}
     * @throws JcommuneUrlNotConfiguredException occurs when the {@code jCommuneUrl} is incorrect
     */
    public void notifyAboutSectionDelete(String jCommuneUrl, PoulpeSection section)
            throws NoConnectionToJcommuneException, JcommuneRespondedWithErrorException,
            JcommuneUrlNotConfiguredException;

    /**
     * Notifies delete the branch
     *
     * @param jCommuneUrl JCommune URL
     * @param branch which will be deleted
     * @throws NoConnectionToJcommuneException some connection problems happened, while trying to notify JCommune
     * @throws JcommuneRespondedWithErrorException occurs when the response status is not {@code OK 200}
     * @throws JcommuneUrlNotConfiguredException occurs when the {@code jCommuneUrl} is incorrect
     */
    public void notifyAboutBranchDelete(String jCommuneUrl, PoulpeBranch branch)
            throws NoConnectionToJcommuneException, JcommuneRespondedWithErrorException,
            JcommuneUrlNotConfiguredException;

    /**
     * Notifies delete the component
     *
     * @param jCommuneUrl JCommune URL
     * @throws NoConnectionToJcommuneException some connection problems happened, while trying to notify JCommune
     * @throws JcommuneRespondedWithErrorException occurs when the response status is not {@code OK 200}
     * @throws JcommuneUrlNotConfiguredException occurs when the {@code jCommuneUrl} is incorrect
     */
    public void notifyAboutComponentDelete(String jCommuneUrl) throws NoConnectionToJcommuneException,
            JcommuneRespondedWithErrorException, JcommuneUrlNotConfiguredException;

    /**
     * Notifies reindex component
     *
     * @param jCommuneUrl JCommune URL
     * @throws NoConnectionToJcommuneException some connection problems happened, while trying to notify JCommune
     * @throws JcommuneRespondedWithErrorException occurs when the response status is not {@code OK 200}
     * @throws JcommuneUrlNotConfiguredException occurs when the {@code jCommuneUrl} is incorrect
     */
    public void notifyAboutReindexComponent(String jCommuneUrl) throws NoConnectionToJcommuneException,
            JcommuneRespondedWithErrorException, JcommuneUrlNotConfiguredException;

}