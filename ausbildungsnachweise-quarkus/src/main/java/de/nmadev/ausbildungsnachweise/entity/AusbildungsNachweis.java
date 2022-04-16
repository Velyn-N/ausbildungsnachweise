package de.nmadev.ausbildungsnachweise.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalDate;

@Getter @Setter
public class AusbildungsNachweis implements Serializable, JsonEntity {

    private Long id;

    private int ausbildungsjahr;
    private String abteilung;
    private boolean daily;

    private LocalDate startDate;
    private LocalDate endDate;

    private Long azubiId;
    private boolean signedByAzubi;
    private LocalDate azubiSignDate;

    private Long ausbilderId;
    private boolean signedByAusbilder;
    private LocalDate ausbilderSignDate;

    @Override
    public boolean isValid() {
        return ausbildungsjahr > 0
                && StringUtils.isNotBlank(abteilung)
                && startDate != null
                && endDate != null
                && azubiId != null
                && ausbilderId != null;
    }
}
