package de.nmadev.ausbildungsnachweise.entity;

public interface JsonEntity {

    public Long getId();

    public void setId(Long id);

    public abstract boolean isValid();
}
