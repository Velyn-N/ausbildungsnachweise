package de.nmadev.ausbildungsnachweise.rest.user;

import de.nmadev.ausbildungsnachweise.CustomGson;
import de.nmadev.ausbildungsnachweise.dao.UserDao;
import de.nmadev.ausbildungsnachweise.entity.User;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

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
            user.setIsAdmin(true);
            return userDao.createUser(user);
        } else if (requestUser.getUser().isAdmin()) {
            log.info("Erstelle Nutzer...");
            return userDao.createUser(user);
        } else {
            log.info("Keine Berechtigung einen Nutzer zu erstellen.");
            return false;
        }
    }

    @POST
    @Path("/change-password")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePassword(PasswordPair passwordPair) {
        if (requestUser.getUser() == null) {
            return Response.status(Response.Status.FORBIDDEN).entity("Du musst eingeloggt sein.").build();
        }
        boolean success = userDao.updatePassword(requestUser.getUser(), passwordPair.oldPassword, passwordPair.newPassword);
        return Response.status(success ? Response.Status.OK : Response.Status.BAD_REQUEST)
                .entity(success)
                .build();
    }

    public record LoginCredentials(String email, String password) {}
    public record PasswordPair(String oldPassword, String newPassword) {}
}
