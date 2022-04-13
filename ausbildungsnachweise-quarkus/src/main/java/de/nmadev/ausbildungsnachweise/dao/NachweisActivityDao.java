package de.nmadev.ausbildungsnachweise.dao;

import de.nmadev.ausbildungsnachweise.JsonStorageFileManager;
import de.nmadev.ausbildungsnachweise.entity.AusbildungsNachweisActivity;
import de.nmadev.ausbildungsnachweise.entity.lists.NachweisActivityList;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class NachweisActivityDao {

    @Inject
    JsonStorageFileManager jsonStorage;
    @Inject
    UserDao userDao;

    public NachweisActivityList getActivitiesOfUser(Long userId) {
        if (userId == null) return new NachweisActivityList();

        return new NachweisActivityList(
                getActivityListFromFile()
                .stream()
                .filter(activity -> userId.equals(activity.getUserId()))
                .toList());
    }

    public void persistNachweisList(NachweisActivityList list) {
        boolean dataOkay = true;
        for (AusbildungsNachweisActivity activity : list) {
            if (StringUtils.isBlank(activity.getActivity())) dataOkay = false;
            if (activity.getDurationHours() < 0 || activity.getDurationHours() > 24) dataOkay = false;
            if (!userDao.userExists(activity.getUserId())) dataOkay = false;

        }
        if (dataOkay) saveActivityListToFile(list);
    }

    private void saveActivityListToFile(NachweisActivityList list) {
        jsonStorage.updateDataFile(JsonStorageFileManager.DataFile.ACTIVITIES, list);
    }

    private NachweisActivityList getActivityListFromFile() {
        return jsonStorage.loadDataFromFile(JsonStorageFileManager.DataFile.ACTIVITIES, NachweisActivityList.class);
    }
}
