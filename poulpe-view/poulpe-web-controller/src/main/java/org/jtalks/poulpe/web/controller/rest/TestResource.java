package org.jtalks.poulpe.web.controller.rest;

import org.restlet.Component;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Protocol;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.restlet.security.ChallengeAuthenticator;

public class TestResource extends ServerResource{

    @Get
    public String toString() {
        return "It is TestResource!";
    }
}
