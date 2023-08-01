package de.nmadev.ausbildungsnachweise.rest.user;

import de.nmadev.ausbildungsnachweise.CustomGson;
import de.nmadev.ausbildungsnachweise.dao.UserDao;
import de.nmadev.ausbildungsnachweise.entity.User;
import lombok.Data;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
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

    @POST
    @Path("/password/change")
    @Consumes(MediaType.APPLICATION_JSON)
    public void changePassword(PasswordChangeParameter passwords) {
        User user = requestUser.getUser();

        if (UserDao.checkPassword(passwords.oldPassword, user.getPassword())) {
            UserDao.changePassword(passwords.newPassword, user);

            userDao.persistUser(user);
        }
    }

    @Data
    public static class LoginCredentials implements Serializable {
        private String email;
        private String password;
    }

    @Data
    public static class PasswordChangeParameter implements Serializable {
        private String oldPassword;
        private String newPassword;
    }
}
