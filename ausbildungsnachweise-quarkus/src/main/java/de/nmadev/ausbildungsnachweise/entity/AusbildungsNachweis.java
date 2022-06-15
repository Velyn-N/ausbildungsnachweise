package de.nmadev.ausbildungsnachweise.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class AusbildungsNachweis implements Serializable, JsonEntity {

    private Long id;

    private int ausbildungsjahr = 0;
    private String abteilung;

    private LocalDate startDate;
    private LocalDate endDate;

    private Long azubiId;
    private boolean signedByAzubi;
    private LocalDateTime azubiSignDate;

    private Long ausbilderId;
    private boolean signedByAusbilder;
    private LocalDateTime ausbilderSignDate;

    private List<NachweisActivity> activities = new ArrayList<>();

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
