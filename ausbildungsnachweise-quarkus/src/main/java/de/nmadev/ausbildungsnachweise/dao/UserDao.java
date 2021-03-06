package de.nmadev.ausbildungsnachweise.dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import de.nmadev.ausbildungsnachweise.JsonStorageFileManager;
import de.nmadev.ausbildungsnachweise.entity.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserDao extends FileDao<User> {

    public List<User> getAusbilderUsers() {
        return super.getEntitiesMatching(User::isAusbilder);
    }

    public Optional<User> getUser(Long userId) {
        return super.getById(userId);
    }

    public Optional<User> loginUser(String email, String password) {
        List<User> users = super.getEntitiesMatching(user -> email.equals(user.getEmail()));
        for (User user : users) {
            boolean passwordCorrect = checkPassword(password, user.getPassword());
            if (passwordCorrect && !user.isLocked()) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public static String hashPassword(String rawPassword) {
        return BCrypt.withDefaults().hashToString(14, rawPassword.toCharArray());
    }

    public static boolean checkPassword(String rawPassword, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), hashedPassword.toCharArray());
        return result.verified;
    }

    @Override
    protected JsonStorageFileManager.DataFile getDataFile() {
        return JsonStorageFileManager.DataFile.USERS;
    }

    @Override
    protected Class<User[]> getArrayType() {
        return User[].class;
    }
}
