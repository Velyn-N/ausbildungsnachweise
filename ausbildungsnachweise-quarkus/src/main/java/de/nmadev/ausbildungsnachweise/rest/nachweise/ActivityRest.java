package de.nmadev.ausbildungsnachweise.rest.nachweise;

import de.nmadev.ausbildungsnachweise.CustomGson;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/rest/activities")
public class ActivityRest {

    @Inject
    CustomGson gson;

    @GET
    @Path("/myactivities")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMyActivities() {
        return "";
    }
}
