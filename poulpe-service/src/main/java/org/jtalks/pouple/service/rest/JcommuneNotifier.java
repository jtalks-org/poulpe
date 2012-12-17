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
package org.jtalks.pouple.service.rest;

import org.apache.commons.lang3.StringUtils;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.service.exceptions.JcommuneRespondedWithErrorException;
import org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguredException;
import org.jtalks.poulpe.service.exceptions.NoConnectionToJcommuneException;
import org.restlet.Context;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * // TODO: write header, update methods comments, delete commented code and dead code, check efficiency
 * @author Evgeny Kapinos
 */
public class JcommuneNotifier {
//    /**
//     * A link which means 'delete the whole component' which will cause all the topics from all the branches to be
//     * removed by JCommune.
//     */
//    private static final String WHOLEFORUM_URL_PART = "/component";
//    /** A URL to trigger re-indexing of forum search engine. */
//    private static final String REINDEX_URL_PART = "/search/index/rebuild";
//    /** URL to ask JCommune to remove content of the specific section. */
//    private static final String SECTIONS_URL_PART = "/sections/";
    /** URL to ask JCommune to remove content of the specific branch. */
    private static final String BRANCH_URL_PART = "/branches/";
    
    /**
     * If either connection can't be established (e.g. firewall drops it) or connection was established, but response is
     * not coming back during this timeout, then the connection will be dropped. Note, that these are milliseconds.
     */
    private static final int CONNECTION_TIMEOUT = 5000;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
//    private final UserDao userDao;
//
//    public JcommuneNotifier(UserDao userDao) {
//        this.userDao = userDao;
//    }

//    /**
//     * Notifies delete the section
//     *
//     * @param jCommuneUrl JCommune Url
//     * @param section     which will be deleted
//     * @throws NoConnectionToJcommuneException
//     *          some connection problems happened, while trying to notify Jcommune
//     * @throws JcommuneRespondedWithErrorException
//     *          occurs when the response status is not in the interval {@link #MIN_HTTP_STATUS} and {@link
//     *          #MAX_HTTP_STATUS}
//     * @throws JcommuneUrlNotConfiguredException
//     *          occurs when the {@code jCommuneUrl} is incorrect
//     */
//    public void notifyAboutSectionDelete(String jCommuneUrl, PoulpeSection section)
//            throws NoConnectionToJcommuneException, JcommuneRespondedWithErrorException,
//            JcommuneUrlNotConfiguredException {
//        long id = section.getId();
//        notifyAboutDeleteElement(jCommuneUrl + SECTIONS_URL_PART + id);
//    }

    /**
     * Notifies delete the branch
     *
     * @param jCommuneUrl JCommune URL
     * @param branch which will be deleted
     * @throws NoConnectionToJcommuneException some connection problems happened, while trying to notify JCommune
     * @throws JcommuneRespondedWithErrorException occurs when the response status is not {@code OK 200}
     * @throws JcommuneUrlNotConfiguredException occurs when the {@code jCommuneUrl} is incorrect
     */
    public void notifyAboutBranchDelete(String jCommuneUrl, PoulpeBranch branch) throws NoConnectionToJcommuneException,
            JcommuneRespondedWithErrorException, JcommuneUrlNotConfiguredException {        
        checkUrlIsConfigured(jCommuneUrl);
        notifyAboutDeleteElement(jCommuneUrl + BRANCH_URL_PART + branch.getId());
    }

//    /**
//     * Notifies delete the component
//     *
//     * @param jCommuneUrl JCommune Url
//     * @throws NoConnectionToJcommuneException
//     *          some connection problems happend, while trying to notify Jcommune
//     * @throws JcommuneRespondedWithErrorException
//     *          occurs when the response status is not in the interval {@code MIN_HTTP_STATUS} and {@code
//     *          MAX_HTTP_STATUS}
//     * @throws JcommuneUrlNotConfiguredException
//     *          occurs when the {@code jCommuneUrl} is incorrect
//     */
//    public void notifyAboutComponentDelete(String jCommuneUrl) throws NoConnectionToJcommuneException,
//            JcommuneRespondedWithErrorException, JcommuneUrlNotConfiguredException {
//        notifyAboutDeleteElement(jCommuneUrl + WHOLEFORUM_URL_PART);
//    }
//
//    /**
//     * Notifies reindex сomponent
//     *
//     * @param jCommuneUrl JCommune Url
//     * @throws NoConnectionToJcommuneException
//     *          some connection problems happened, while trying to notify Jcommune
//     * @throws JcommuneRespondedWithErrorException
//     *          occurs when the response status is not in the interval {@code MIN_HTTP_STATUS} and {@code
//     *          MAX_HTTP_STATUS}
//     * @throws org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguredException
//     *          occurs when the {@code jCommuneUrl} is incorrect
//     */
//    public void notifyAboutReindexComponent(String jCommuneUrl) throws NoConnectionToJcommuneException,
//            JcommuneRespondedWithErrorException, JcommuneUrlNotConfiguredException {
//        checkUrlIsConfigured(jCommuneUrl);
//        createAndSendRequest(jCommuneUrl + REINDEX_URL_PART, HttpPost.METHOD_NAME);
//    }
//
    /**
     * Notifies JCommune that an element is about to be deleted (for instance Component, Branch, Section).
     *
     * @param url full URL for REST request  
     * @throws NoConnectionToJcommuneException some connection problems happened, while trying to notify JCommune
     * @throws JcommuneRespondedWithErrorException occurs when the response status is not {@code OK 200}
     * @throws JcommuneUrlNotConfiguredException occurs when the {@code jCommuneUrl} is incorrect
     */
    protected void notifyAboutDeleteElement(String url) throws NoConnectionToJcommuneException,
            JcommuneRespondedWithErrorException {
        
        ClientResource clientResource = new ClientResource(new Context(), url);
        
        // Set timeout as described over here: 
        // http://wiki.restlet.org/docs_2.0/13-restlet/27-restlet/325-restlet/37-restlet.html
        clientResource.getContext().getParameters().add("socketTimeout", String.valueOf(CONNECTION_TIMEOUT));
        
        logger.info("Sending DELETE request to JCommune: [{}]", url);
        
        try{
            clientResource.delete();            
        } catch (ResourceException e){
            processResourceException(e);
        }
        
        logger.info("OK");       
    }

    /**     
     * Checks the URL
     *
     * @param jCommuneUrl JCommune Url
     * @throws JcommuneUrlNotConfiguredException occurs when the {@code jCommuneUrl is incorrect
     */
    protected void checkUrlIsConfigured(String jCommuneUrl) throws JcommuneUrlNotConfiguredException {
        if (StringUtils.isBlank(jCommuneUrl)) {
            throw new JcommuneUrlNotConfiguredException();
        }
    }

    /**
     * Logs an error that was received from JCommune in the REST response. 
     *
     * @param e exception from REST framework
     * @throws JcommuneRespondedWithErrorException
     *          if REST request reached JCommune, but JCommune responded with error code, such situation may happen for
     *          instance when we're deleting some branch, but it was already deleted, or JCommune has troubles removing
     *          that branch (database connection lost). Note that if we reach some other site and it responds with 404
     *          for example, this will be still this error.
     * @throws NoConnectionToJcommuneException
     *          if nothing was found at the specified URL, note that if URL was set incorrectly to point to another
     *          site, this can't be figured out by us, we just operate with HTTP codes, which means that either the
     *          request will be fine or {@link JcommuneRespondedWithErrorException} might be thrown in case if some
     *          other site was specified and it returned 404
     */
    private void processResourceException(ResourceException e) throws NoConnectionToJcommuneException, JcommuneRespondedWithErrorException {
        logger.error("JCommune error response due to exception.", e);
        if (e.getStatus().isConnectorError()){
            throw new NoConnectionToJcommuneException(e);
        } else{
            throw new JcommuneRespondedWithErrorException(e);
        }
    }
}
