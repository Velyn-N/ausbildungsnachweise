package de.nmadev.ausbildungsnachweise.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalDate;

@Getter @Setter
public class NachweisActivity implements Serializable, JsonEntity {

    private Long id;

    private Long userId;
    private LocalDate date;
    private String activity;
    private double durationHours;

    @Override
    public boolean isValid() {
        return userId != null
                && userId != -1L
                && date != null
                && StringUtils.isNotBlank(activity)
                && 0 < durationHours
                && 24 >= durationHours;
    }

    @Override
    public String toString() {
        return "NachweisActivity{" +
                "id=" + id +
                ", userId=" + userId +
                ", date=" + date +
                ", activity='" + activity + '\'' +
                ", durationHours=" + durationHours +
                '}';
    }
}
