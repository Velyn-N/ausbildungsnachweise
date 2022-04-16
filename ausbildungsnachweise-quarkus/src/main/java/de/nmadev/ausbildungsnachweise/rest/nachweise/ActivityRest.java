package de.nmadev.ausbildungsnachweise.rest.nachweise;

import de.nmadev.ausbildungsnachweise.CustomGson;
import de.nmadev.ausbildungsnachweise.dao.ActivityDao;
import de.nmadev.ausbildungsnachweise.entity.NachweisActivity;
import de.nmadev.ausbildungsnachweise.entity.lists.NachweisActivityList;
import de.nmadev.ausbildungsnachweise.rest.user.RequestUserBean;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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
        return gson.toJson(activityDao.getActivitiesOfUser(requestUser.getUserId()));
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
}
