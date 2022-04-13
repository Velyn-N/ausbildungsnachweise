package de.nmadev.ausbildungsnachweise.entity.lists;

import de.nmadev.ausbildungsnachweise.entity.AusbildungsNachweisActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * A Class extending {@link ArrayList}<{@link AusbildungsNachweisActivity}> for use with Gson.
 */
public class NachweisActivityList extends ArrayList<AusbildungsNachweisActivity> {

    public NachweisActivityList() {}

    public NachweisActivityList(Collection<AusbildungsNachweisActivity> list) {
        super(list);
    }

    public NachweisActivityList(AusbildungsNachweisActivity... activities) {
        super(Arrays.asList(activities));
    }
}
