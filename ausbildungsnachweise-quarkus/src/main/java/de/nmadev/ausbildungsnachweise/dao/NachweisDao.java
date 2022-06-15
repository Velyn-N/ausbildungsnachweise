package de.nmadev.ausbildungsnachweise.dao;

import de.nmadev.ausbildungsnachweise.JsonStorageFileManager;
import de.nmadev.ausbildungsnachweise.entity.AusbildungsNachweis;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class NachweisDao extends FileDao<AusbildungsNachweis> {

    public List<AusbildungsNachweis> getNachweiseForUser(Long userId) {
        return (userId != null)
                ? super.getEntitiesMatching(nw -> userId.equals(nw.getAzubiId()) || userId.equals(nw.getAusbilderId()))
                : new ArrayList<>();
    }

    public Optional<AusbildungsNachweis> getNachweis(Long id) {
        return super.getById(id);
    }

    public void deleteNachweis(Long id) {
        super.deleteEntity(id);
    }

    public void persistNachweis(AusbildungsNachweis nachweis) {
        super.saveEntity(nachweis);
    }

    @Override
    protected JsonStorageFileManager.DataFile getDataFile() {
        return JsonStorageFileManager.DataFile.NACHWEISE;
    }

    @Override
    protected Class<AusbildungsNachweis[]> getArrayType() {
        return AusbildungsNachweis[].class;
    }
}
