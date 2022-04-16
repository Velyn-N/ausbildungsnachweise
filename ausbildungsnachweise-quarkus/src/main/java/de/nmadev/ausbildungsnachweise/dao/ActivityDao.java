package de.nmadev.ausbildungsnachweise.dao;

import de.nmadev.ausbildungsnachweise.JsonStorageFileManager;
import de.nmadev.ausbildungsnachweise.entity.NachweisActivity;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ActivityDao extends FileDao<NachweisActivity> {

    public List<NachweisActivity> getActivitiesOfUser(Long userId) {
        return (userId != null) ? super.getEntitiesMatching(act -> userId.equals(act.getUserId())) : new ArrayList<>();
    }

    public void persistActivity(NachweisActivity activity) {
        super.saveEntity(activity);
    }

    public void persistActivityList(List<NachweisActivity> list) {
        super.saveEntityList(list);
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
