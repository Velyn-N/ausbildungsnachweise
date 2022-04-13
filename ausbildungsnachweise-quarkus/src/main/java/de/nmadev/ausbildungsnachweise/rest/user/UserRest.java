package de.nmadev.ausbildungsnachweise.rest.user;

import de.nmadev.ausbildungsnachweise.CustomGson;
import de.nmadev.ausbildungsnachweise.dao.UserDao;
import de.nmadev.ausbildungsnachweise.entity.User;
import lombok.Data;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;

@Path("/rest/user")
public class UserRest {

    @Inject
    CustomGson gson;
    @Inject
    RequestUserBean requestUser;
    @Inject
    UserDao userDao;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String login(LoginCredentials credentials) {
        return requestUser.loginAndGetJWT(credentials.email, credentials.password);
    }

    @GET
    @Path("/self")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUser() {
        User user = requestUser.getUser();
        return gson.toJson(user);
    }

    @GET
    @Path("/ausbilder/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllAusbilder() {
        return gson.toJson(userDao.getAusbilderUsers());
    }

    @Data
    public static class LoginCredentials implements Serializable {
        private String email;
        private String password;
    }
}
