package org.jtalks.poulpe.web.controller.rest;

import org.apache.http.HttpStatus;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.service.exceptions.UserExistException;
import org.restlet.data.Status;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;

import java.io.IOException;

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
        JaxbRepresentation<Registration> resultRep = new JaxbRepresentation<Registration>(new Registration());
        try {
            JaxbRepresentation<User> userRep = new JaxbRepresentation<User>(represent, User.class);
            User user = userRep.getObject();
            userService.registration(user.getUsername(),user.getPasswordHash(),user.getFirstName(),user.getLastName(),user.getEmail());
        } catch (Exception e) {
            Registration registration = new Registration();
            registration.setErrorMessage(e.getMessage());
            resultRep = new JaxbRepresentation<Registration>(registration);
            getResponse().setStatus(new Status(HttpStatus.SC_BAD_REQUEST));
            return resultRep;
        }
        return resultRep;
    }
}
