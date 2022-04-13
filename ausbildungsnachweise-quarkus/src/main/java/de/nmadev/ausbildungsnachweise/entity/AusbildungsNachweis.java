package de.nmadev.ausbildungsnachweise.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter
public class AusbildungsNachweis implements Serializable {

    private Long id;

    private int ausbildungsjahr;
    private String abteilung;
    private boolean daily;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Long azubiId;
    private boolean signedByAzubi;
    private LocalDateTime azubiSignDate;

    private Long ausbilderId;
    private boolean signedByAusbilder;
    private LocalDateTime ausbilderSignDate;
}
