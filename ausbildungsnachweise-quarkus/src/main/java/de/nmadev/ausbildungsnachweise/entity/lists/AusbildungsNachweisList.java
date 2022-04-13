package de.nmadev.ausbildungsnachweise.entity.lists;

import de.nmadev.ausbildungsnachweise.entity.AusbildungsNachweis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * A Class extending {@link ArrayList}<{@link AusbildungsNachweis}> for use with Gson.
 */
public class AusbildungsNachweisList extends ArrayList<AusbildungsNachweis> {

    public AusbildungsNachweisList() {}

    public AusbildungsNachweisList(Collection<AusbildungsNachweis> list) {
        super(list);
    }

    public AusbildungsNachweisList(AusbildungsNachweis... nachweise) {
        super(Arrays.asList(nachweise));
    }
}
