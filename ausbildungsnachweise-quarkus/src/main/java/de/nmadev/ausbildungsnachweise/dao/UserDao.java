package de.nmadev.ausbildungsnachweise.dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import de.nmadev.ausbildungsnachweise.JsonStorageFileManager;
import de.nmadev.ausbildungsnachweise.entity.User;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
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

    public boolean createUser(User user) {
        if (user == null) {
            return false;
        }
        user.setPassword(hashPassword(user.getPassword()));
        boolean success = super.saveEntity(user);
        if (success) {
            log.info("Neuer Benutzer angelegt: {}", user);
        }
        if (!success) {
            log.warn("Could not create new User: {}", user);
            log.warn("Benutzer müssen eine E-Mail, ein Passwort und einen vollen Namen haben. Außerdem müssen sie Azubi, Ausbilder oder Admin sein.");
        }
        return success;
    }

    public boolean updatePassword(User user, String oldPassword, String newPassword) {
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            return false;
        }
        if (!checkPassword(oldPassword, user.getPassword())) {
            return false;
        }
        user.setPassword(hashPassword(newPassword));
        return super.saveEntity(user);
    }

    public int countUsers() {
        return super.getAllEntities().size();
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
