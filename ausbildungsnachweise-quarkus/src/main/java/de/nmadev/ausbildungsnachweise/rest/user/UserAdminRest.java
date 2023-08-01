package de.nmadev.ausbildungsnachweise.rest.user;

import de.nmadev.ausbildungsnachweise.CustomGson;
import de.nmadev.ausbildungsnachweise.dao.UserDao;
import de.nmadev.ausbildungsnachweise.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.Optional;

@Slf4j
@Path("/rest/useradmin")
public class UserAdminRest {

    @Inject
    RequestUserBean requestUser;
    @Inject
    CustomGson gson;
    @Inject
    UserDao userDao;

    /**
     * Only accessible to Admins.<br>
     * Available for everyone if no Users exist (after initial Setup).
     */
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllUsers() {
        return (checkAccess()) ? gson.toJson(userDao.getAllUsers()) : null;
    }

    /**
     * Creates the given User.<br>
     * Hashes the transferred Password String before saving it.<br>
     * <br>
     * Only accessible to Admins.<br>
     * Available for everyone if no Users exist (after initial Setup).
     */
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public void createUser(String json) {
        if (!checkAccess()) return;

        User user = gson.fromJson(json, User.class);

        if (user != null && userDao.getUserByEmail(user.getEmail()).isEmpty()) {
            UserDao.changePassword(user.getPassword(), user);

            userDao.persistUser(user);
        }
    }

    @POST
    @Path("/edit")
    @Consumes(MediaType.APPLICATION_JSON)
    public void editUser(User changedUser) {
        if (!requestUser.getUser().isAdmin()) return;
        if (changedUser == null) return;

        Optional<User> oldUserOpt = userDao.getUser(changedUser.getId());
        if (oldUserOpt.isEmpty()) return;

        User oldUser = oldUserOpt.get();
        boolean constantsChanged = StringUtils.equals(oldUser.getEmail(), changedUser.getEmail());

        if (!constantsChanged) {
            userDao.persistUser(changedUser);
        }
    }

    private boolean checkAccess() {
        return requestUser.getUser().isAdmin() || userDao.getAllUsers().isEmpty();
    }
}
