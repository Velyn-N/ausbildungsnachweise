package de.nmadev.ausbildungsnachweise.dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import de.nmadev.ausbildungsnachweise.JsonStorageFileManager;
import de.nmadev.ausbildungsnachweise.entity.User;
import de.nmadev.ausbildungsnachweise.entity.lists.UserList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserDao {

    @Inject
    JsonStorageFileManager jsonStorage;

    public UserList getAusbilderUsers() {
        return new UserList(getUserListFromFile()
                .stream()
                .filter(User::isAusbilder)
                .toList());
    }

    public Optional<User> getUser(Long userId) {
        return (userId == null) ? Optional.empty() : getUserListFromFile()
                .stream()
                .filter(user -> userId.equals(user.getId()))
                .findAny();
    }

    public boolean userExists(Long userId) {
        return getUser(userId).isPresent();
    }

    public Optional<User> loginUser(String email, String password) {
        List<User> users = getUserListFromFile().stream().filter(user -> email.equals(user.getEmail())).toList();
        for (User user : users) {
            boolean passwordCorrect = checkPassword(password, user.getPassword());
            if (passwordCorrect && !user.isLocked()) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    private UserList getUserListFromFile() {
        return jsonStorage.loadDataFromFile(JsonStorageFileManager.DataFile.USERS, UserList.class);
    }

    public static String hashPassword(String rawPassword) {
        return BCrypt.withDefaults().hashToString(14, rawPassword.toCharArray());
    }

    public static boolean checkPassword(String rawPassword, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), hashedPassword.toCharArray());
        return result.verified;
    }
}
