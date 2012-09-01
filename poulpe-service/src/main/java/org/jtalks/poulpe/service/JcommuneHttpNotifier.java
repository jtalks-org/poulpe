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

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.exceptions.JcommuneRespondedWithErrorException;
import org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguredException;
import org.jtalks.poulpe.service.exceptions.NoConnectionToJcommuneException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Notifier to notify JCommune component about elements' deleting. It is usefull to help forum keep such information, as
 * user's messages count, up to date.
 *
 * @author Nickolay Polyarniy
 * @author Mikhail Zaitsev
 */
public class JcommuneHttpNotifier {

    /**
     * Minimum value of a successful status, less than that is an error HTTP response.
     */
    private static final int MIN_HTTP_STATUS = 200;
    /**
     * Maximum value of a successful status, starting from 300 means an error response.
     */
    private static final int MAX_HTTP_STATUS = 299;
    /**
     * A link which means 'delete the whole component' which will cause all the topics from all the branches to be
     * removed by JCommune.
     */
    private static final String WHOLEFORUM_URL_PART = "/component";
    /**
     * A URL to trigger re-indexing of forum search engine.
     */
    private static final String REINDEX_URL_PART = "/search/index/rebuild";
    /**
     * URL to ask JCommune to remove content of the specific section.
     */
    private static final String SECTIONS_URL_PART = "/sections/";
    /**
     * URL to ask JCommune to remove content of the specific branch.
     */
    private static final String BRANCH_URL_PART = "/branches/";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Notifies delete the section
     *
     * @param jCommuneUrl JCommune Url
     * @param section     which will be deleted
     * @throws NoConnectionToJcommuneException
     *          some connection problems happened, while trying to notify Jcommune
     * @throws JcommuneRespondedWithErrorException
     *          occurs when the response status is not in the interval {@link #MIN_HTTP_STATUS} and {@link
     *          #MAX_HTTP_STATUS}
     * @throws org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguredException
     *          occurs when the {@code #jCommuneUrl} is incorrect
     */
    public void notifyAboutSectionDelete(String jCommuneUrl, PoulpeSection section)
        throws NoConnectionToJcommuneException, JcommuneRespondedWithErrorException, JcommuneUrlNotConfiguredException {
        long id = section.getId();
        notifyAboutDeleteElement(jCommuneUrl + SECTIONS_URL_PART + id);
    }

    /**
     * Notifies delete the branch
     *
     * @param jCommuneUrl JCommune Url
     * @param branch      which will be deleted
     * @throws NoConnectionToJcommuneException
     *          some connection problems happened, while trying to notify Jcommune
     * @throws JcommuneRespondedWithErrorException
     *          occurs when the response status is not in the interval {@link #MIN_HTTP_STATUS} and {@link
     *          #MAX_HTTP_STATUS}
     * @throws org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguredException
     *          occurs when the {@code #jCommuneUrl} is incorrect
     */
    public void notifyAboutBranchDelete(String jCommuneUrl, PoulpeBranch branch)
        throws NoConnectionToJcommuneException, JcommuneRespondedWithErrorException, JcommuneUrlNotConfiguredException {
        long id = branch.getId();
        notifyAboutDeleteElement(jCommuneUrl + BRANCH_URL_PART + id);
    }

    /**
     * Notifies delete the component
     *
     * @param jCommuneUrl JCommune Url
     * @throws {@link NoConnectionToJcommuneException} some connection problems happend, while trying to notify
     *                Jcommune
     * @throws {@link JcommuneRespondedWithErrorException} occurs when the response status is not in the interval {@code
     *                MIN_HTTP_STATUS} and {@code MAX_HTTP_STATUS}
     * @throws {@link org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguredException} occurs when the {@code
     *                jCommuneUrl} is incorrect
     */
    public void notifyAboutComponentDelete(String jCommuneUrl)
        throws NoConnectionToJcommuneException, JcommuneRespondedWithErrorException, JcommuneUrlNotConfiguredException {
        notifyAboutDeleteElement(jCommuneUrl + WHOLEFORUM_URL_PART);
    }

    /**
     * Notifies JCommune that an element is about to be deleted (for instance Component, Branch, Section).
     *
     * @param url JCommune Url
     * @throws {@link NoConnectionToJcommuneException} some connection problems happend, while trying to notify
     *                Jcommune
     * @throws {@link JcommuneRespondedWithErrorException} occurs when the response status is not in the interval {@code
     *                MIN_HTTP_STATUS} and {@code MAX_HTTP_STATUS}
     * @throws {@link org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguredException} occurs when the {@code
     *                url} is incorrect
     */
    protected void notifyAboutDeleteElement(String url)
        throws NoConnectionToJcommuneException, JcommuneRespondedWithErrorException, JcommuneUrlNotConfiguredException {
        checkUrlIsConfigured(url);
        try {
            HttpResponse response = sendHttpRequest(url,HttpDelete.METHOD_NAME);
            int statusCode = response.getStatusLine().getStatusCode();
            logger.info("Status response notify about delete element JCommune: "+statusCode+"; URL request: " + url);
            if (statusCode < MIN_HTTP_STATUS || statusCode > MAX_HTTP_STATUS) {
                throw new JcommuneRespondedWithErrorException();
            }
        } catch (IOException e) {
            throw new NoConnectionToJcommuneException();
        }
    }

    /**
     * Checks the url
     *
     * @param jCommuneUrl JCommune Url
     * @throws {@link org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguredException} occurs when the {@code
     *                jCommuneUrl} is incorrect
     */
    protected void checkUrlIsConfigured(String jCommuneUrl) throws JcommuneUrlNotConfiguredException {
        if (StringUtils.isBlank(jCommuneUrl)) {
            throw new JcommuneUrlNotConfiguredException();
        }
    }

    /**
     * Notifies reindex сomponent
     *
     * @param jCommuneUrl JCommune Url
     * @throws NoConnectionToJcommuneException
     *          some connection problems happend, while trying to notify Jcommune
     * @throws JcommuneRespondedWithErrorException
     *          occurs when the response status is not in the interval {@code MIN_HTTP_STATUS} and {@code
     *          MAX_HTTP_STATUS}
     * @throws org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguredException
     *          occurs when the {@code jCommuneUrl} is incorrect
     */
    public void notifyAboutReindexComponent(String jCommuneUrl)
        throws NoConnectionToJcommuneException, JcommuneRespondedWithErrorException, JcommuneUrlNotConfiguredException {
        checkUrlIsConfigured(jCommuneUrl);
        String reindexUrl = jCommuneUrl + REINDEX_URL_PART;
        try {
            HttpResponse response = sendHttpRequest(reindexUrl,HttpPost.METHOD_NAME);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode < MIN_HTTP_STATUS || statusCode > MAX_HTTP_STATUS) {
                throw new JcommuneRespondedWithErrorException(String.valueOf(statusCode));
            }
        } catch (IOException e) {
            throw new NoConnectionToJcommuneException();
        }
    }

    @VisibleForTesting
    HttpResponse sendHttpRequest(String reindexUrl, String httpMethod) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpUriRequest emptyRequest = null;
        if(HttpDelete.METHOD_NAME.equals(httpMethod)){
            emptyRequest = new HttpDelete(reindexUrl);
        }else if(HttpPost.METHOD_NAME.equals(httpMethod)){
            emptyRequest = new HttpPost(reindexUrl);
        }
        return httpClient.execute(emptyRequest);
    }


}
