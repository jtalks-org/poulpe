package org.jtalks.poulpe.web.controller.rest;

import org.jtalks.poulpe.service.UserService;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;

/**
 * Implementation registration resource for users
 *
 * @author Mikhail Zaitsev
 */
public class UserRegistrationResource extends ServerResource implements RegistrationResource{

    private UserService userService;

    public UserRegistrationResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers user
     *
     * @param represent request representation
     * @return response representation
     */
    @Override
    public Representation register(Representation represent) {
        Authentication authentication = new Authentication();
        Profile result = new Profile();
        result.setFirstName("firstName");
        result.setLastName("lastName");
        authentication.setProfile(result);
        JaxbRepresentation<Authentication> resultRep = new JaxbRepresentation<Authentication>(authentication);
        resultRep.setFormattedOutput(true);
        return resultRep;
    }
}
