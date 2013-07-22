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
package org.jtalks.poulpe.web.controller.rest.authenticator;

import org.apache.log4j.Logger;
import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.poulpe.service.ComponentService;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.security.MapVerifier;
import org.restlet.security.Verifier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * Verifier for restlet Authenticator. Username and password are loading from properties of ADMIN_PANEL component.
 * If username is empty, the authentication is not required.
 *
 */
public class PropertyVerifier extends MapVerifier{

    private static final Logger LOGGER = Logger.getLogger(PropertyVerifier.class);

    public static final String USERNAME_PROP= "poulpe.rest_login";
    public static final String PASSWORD_PROP= "poulpe.rest_password";

    private ComponentService componentService;

    public PropertyVerifier(ComponentService componentService) {
        this.componentService = componentService;
    }

    /**
     * Loads username and password from properties of the ADMIN_PANEL component,
     * Sets local secrets,
     *
     * @return username or null
     */
    public String loadCredentialsFromProperties(){
        Component component = componentService.getByType(ComponentType.ADMIN_PANEL);
        if(component == null){
            LOGGER.error("The component of type ADMIN_PANEL was not found.");
            return null;
        }
        String username = component.getProperty(USERNAME_PROP);
        String password = component.getProperty(PASSWORD_PROP);

        if(username==null){
            LOGGER.error("The "+USERNAME_PROP+" property is NULL.");
            return null;
        }
        if(password==null){
            LOGGER.error("The "+PASSWORD_PROP+" property is NULL.");
            return null;
        }
        ConcurrentHashMap<String, char[]> localSecrets = new ConcurrentHashMap<String, char[]>();
        localSecrets.put(username,password.toCharArray());
        synchronized (getLocalSecrets()) {
                getLocalSecrets().clear();
                getLocalSecrets().putAll(localSecrets);
        }
        return username;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int verify(Request request, Response response) {
        String username =loadCredentialsFromProperties();
        if(username!=null && username.isEmpty()){
            char[] password = getLocalSecret(username);
            if(password.length==0){
                return Verifier.RESULT_VALID;
            }
        }
        return super.verify(request, response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocalSecrets(Map<String, char[]> localSecrets) {
        throw new UnsupportedOperationException("Username and password are loading from properties of component.");
    }
}
