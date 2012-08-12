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
import org.jtalks.poulpe.service.exceptions.ElementDoesNotExist;

import java.io.IOException;

/**
 * Class provide ability to notify JCommune about changes, such as deleting section.
 *
 * @author Nickolay Polyarniy
 */
public class JcommuneHttpNotifier {

    private final String JCOMMUNE_URL = "http://localhost:8080/jcommune";

    /**
     * @param section to be delete
     * @throws IOException some problem with connection to JCommune happend
     * @throws org.jtalks.poulpe.service.exceptions.ElementDoesNotExist
     *                     if JCommune return 204 status code. That means that resource does not exist
     */
    public void notifyAboutSectionDelete(PoulpeSection section) throws IOException, ElementDoesNotExist {
        long id = section.getId();
        notifyAboutDeleteElement(JCOMMUNE_URL + "/sections/" + id);
    }

    /**
     * Notifies delete the component
     *
     * @throws IOException some problem with connection to JCommune happend
     * @throws org.jtalks.poulpe.service.exceptions.ElementDoesNotExist
     *                     if JCommune return 204 status code. That means that resource does not exist
     */
    public void notifyAboutComponentDelete() throws ElementDoesNotExist, IOException {
        notifyAboutDeleteElement(JCOMMUNE_URL + "/wholeforum");
    }

    private void notifyAboutDeleteElement(String url) throws ElementDoesNotExist, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpDelete deleteRequest = new HttpDelete(url);
        HttpResponse response = httpClient.execute(deleteRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 204 || statusCode == 404) {
            throw new ElementDoesNotExist();
        }
    }

}
