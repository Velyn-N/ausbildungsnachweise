package de.nmadev.ausbildungsnachweise.rest.user;

import de.nmadev.ausbildungsnachweise.CustomGson;
import de.nmadev.ausbildungsnachweise.dao.UserDao;
import de.nmadev.ausbildungsnachweise.entity.User;
import lombok.Data;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
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

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public int countUsers() {
        return userDao.countUsers();
    }

    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean createNewUser(User user) {
        if (userDao.countUsers() == 0) {
            log.info("Erstelle initialen Admin-Nutzer...");
            user.setAdmin(true);
            return userDao.createUser(user);
        } else if (requestUser.getUser().isAdmin()) {
            log.info("Erstelle Nutzer...");
            return userDao.createUser(user);
        } else {
            log.info("Keine Berechtigung einen Nutzer zu erstellen.");
            return false;
        }
    }

    @Data
    public static class LoginCredentials implements Serializable {
        private String email;
        private String password;
    }
}
