package de.nmadev.ausbildungsnachweise.entity.lists;

import de.nmadev.ausbildungsnachweise.entity.NachweisActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * A Class extending {@link ArrayList}<{@link NachweisActivity}> for use with Gson.
 */
public class NachweisActivityList extends ArrayList<NachweisActivity> {

    public NachweisActivityList() {}

    public NachweisActivityList(Collection<NachweisActivity> list) {
        super(list);
    }

    public NachweisActivityList(NachweisActivity... activities) {
        super(Arrays.asList(activities));
    }
}
