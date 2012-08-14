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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.exceptions.JcommuneRespondedWithErrorException;
import org.jtalks.poulpe.service.exceptions.NoConnectionToJcommuneException;

import java.io.IOException;

/**
 * Notifier to notify JCommune component about elements' deleting. It is usefull to help forum keep such information,
 * as user's messages count, up to date.
 *
 * @author Nickolay Polyarniy
 */
public class JcommuneHttpNotifier {

    private final String JCOMMUNE_URL = "http://localhost:8080/jcommune";

    /**
     * Notifies delete the section
     *
     * @param section which will be deleted
     * @throws org.jtalks.poulpe.service.exceptions.NoConnectionToJcommuneException some connection problems happend, while trying to notify jCommune
     */
    public void notifyAboutSectionDelete(PoulpeSection section)
        throws NoConnectionToJcommuneException,JcommuneRespondedWithErrorException {
        long id = section.getId();
        notifyAboutDeleteElement(JCOMMUNE_URL + "/sections/" + id);
    }

    /**
     * Notifies delete the component
     *
     * @throws org.jtalks.poulpe.service.exceptions.NoConnectionToJcommuneException some connection problems happend, while trying to notify jCommune
     */
    public void notifyAboutComponentDelete()
        throws NoConnectionToJcommuneException, JcommuneRespondedWithErrorException {
        notifyAboutDeleteElement(JCOMMUNE_URL + "/wholeforum");
    }

    private void notifyAboutDeleteElement(String url)
        throws NoConnectionToJcommuneException,JcommuneRespondedWithErrorException {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpDelete deleteRequest = new HttpDelete(url);
            HttpResponse response = null;
            response = httpClient.execute(deleteRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode<200 || statusCode>299){
                throw new JcommuneRespondedWithErrorException();
            }
        } catch (IOException e) {
            throw new NoConnectionToJcommuneException();
        }
    }

}
