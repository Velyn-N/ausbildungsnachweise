package de.nmadev.ausbildungsnachweise.dao;

import de.nmadev.ausbildungsnachweise.JsonStorageFileManager;
import de.nmadev.ausbildungsnachweise.entity.NachweisActivity;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ActivityDao extends FileDao<NachweisActivity> {

    public List<NachweisActivity> getActivitiesOfUser(Long userId) {
        return (userId != null) ? super.getEntitiesMatching(act -> userId.equals(act.getUserId())) : new ArrayList<>();
    }

    public Optional<NachweisActivity> getActivity(Long id) {
        return super.getById(id);
    }

    public void deleteActivity(Long id) {
        super.deleteEntity(id);
    }

    public void persistActivity(NachweisActivity activity) {
        super.saveEntity(activity);
    }

    @Override
    protected JsonStorageFileManager.DataFile getDataFile() {
        return JsonStorageFileManager.DataFile.ACTIVITIES;
    }

    @Override
    protected Class<NachweisActivity[]> getArrayType() {
        return NachweisActivity[].class;
    }
}
