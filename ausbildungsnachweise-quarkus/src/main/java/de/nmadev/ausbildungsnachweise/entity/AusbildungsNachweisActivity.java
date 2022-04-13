package de.nmadev.ausbildungsnachweise.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter
public class AusbildungsNachweisActivity implements Serializable {

    private Long id;

    private Long userId;
    private LocalDateTime date;
    private String activity;
    private double durationHours;
}
