package de.nmadev.ausbildungsnachweise.rest.nachweise;

import de.nmadev.ausbildungsnachweise.CustomGson;
import de.nmadev.ausbildungsnachweise.dao.ActivityDao;
import de.nmadev.ausbildungsnachweise.entity.NachweisActivity;
import de.nmadev.ausbildungsnachweise.entity.lists.NachweisActivityList;
import de.nmadev.ausbildungsnachweise.rest.user.RequestUserBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Comparator;
import java.util.Optional;

@Path("/rest/activities")
public class ActivityRest {

    @Inject
    CustomGson gson;
    @Inject
    RequestUserBean requestUser;
    @Inject
    ActivityDao activityDao;

    @GET
    @Path("/myactivities")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMyActivities() {
        return gson.toJson(activityDao.getActivitiesOfUser(requestUser.getUserId())
                .stream()
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .toList());
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addActivity(String json) {
        if (StringUtils.isNotBlank(json)) {
            NachweisActivity activity = gson.fromJson(json, NachweisActivity.class);

            if (activity != null) {
                activity.setUserId(requestUser.getUserId());
                activityDao.persistActivity(activity);
            }
        }
    }

    @POST
    @Path("/change")
    @Consumes(MediaType.APPLICATION_JSON)
    public void changeActivity(String json) {
        if (StringUtils.isNotBlank(json)) {
            NachweisActivity activity = gson.fromJson(json, NachweisActivity.class);

            if (activity != null) {
                activityDao.persistActivity(activity);
            }
        }
    }
    
    @POST
    @Path("/delete")
    @Consumes(MediaType.WILDCARD)
    public void deleteActivity(String idStr) {
        System.out.println(idStr);
        long idLong = NumberUtils.toLong(idStr, -1);
        if (idLong > 0) {
            Optional<NachweisActivity> activityOptional = activityDao.getActivity(idLong);

            if (activityOptional.isPresent()) {
                NachweisActivity activity = activityOptional.get();

                if (activity.getUserId().equals(requestUser.getUserId())) {
                    activityDao.deleteActivity(activity.getId());
                }
            }
        }
    }
}
