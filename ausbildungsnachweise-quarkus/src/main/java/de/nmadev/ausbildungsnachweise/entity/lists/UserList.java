package de.nmadev.ausbildungsnachweise.entity.lists;

import de.nmadev.ausbildungsnachweise.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * A Class extending {@link ArrayList}<{@link User}> for use with Gson.
 */
public class UserList extends ArrayList<User> {

    public UserList() {}

    public UserList(Collection<User> list) {
        super(list);
    }

    public UserList(User... users) {
        super(Arrays.asList(users));
    }
}
