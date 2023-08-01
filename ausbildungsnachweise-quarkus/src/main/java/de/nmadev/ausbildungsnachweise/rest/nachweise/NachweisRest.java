package de.nmadev.ausbildungsnachweise.rest.nachweise;

import de.nmadev.ausbildungsnachweise.CustomGson;
import de.nmadev.ausbildungsnachweise.dao.NachweisDao;
import de.nmadev.ausbildungsnachweise.entity.AusbildungsNachweis;
import de.nmadev.ausbildungsnachweise.rest.user.RequestUserBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Path("/rest/nachweise")
public class NachweisRest {

    @Inject
    CustomGson gson;
    @Inject
    RequestUserBean requestUser;
    @Inject
    NachweisDao nachweisDao;

    @GET
    @Path("/mynachweise")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllNachweise() {
        return gson.toJson(nachweisDao.getNachweiseForUser(requestUser.getUserId())
                .stream()
                .sorted((a, b) -> b.getStartDate().compareTo(a.getStartDate()))
                .toList());
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public void createNachweis(String json) {
        if (StringUtils.isBlank(json)) return;
        AusbildungsNachweis nachweis = gson.fromJson(json, AusbildungsNachweis.class);

        if (nachweis == null) return;

        boolean isUserOkay = requestUser.getUserId().equals(nachweis.getAzubiId());
        boolean isNotSigned = !(nachweis.isSignedByAzubi() || nachweis.isSignedByAusbilder());

        if (isUserOkay && isNotSigned) {
            nachweisDao.persistNachweis(nachweis);
        }
    }

    @POST
    @Path("/change")
    @Consumes(MediaType.APPLICATION_JSON)
    public void changeNachweis(String json) {
        if (StringUtils.isNotBlank(json)) {
            AusbildungsNachweis newNachweis = gson.fromJson(json, AusbildungsNachweis.class);

            if (newNachweis != null) {
                Optional<AusbildungsNachweis> savedNachweis = nachweisDao.getNachweis(newNachweis.getId());

                if (savedNachweis.isEmpty()) {
                    nachweisDao.persistNachweis(newNachweis);
                    return;
                }
                AusbildungsNachweis oldNachweis = savedNachweis.get();

                if (isNachweisChangeValid(oldNachweis, newNachweis)) {
                    nachweisDao.persistNachweis(newNachweis);
                }
            }
        }
    }

    private boolean isNachweisChangeValid(AusbildungsNachweis oldNw, AusbildungsNachweis newNw) {
        boolean isAzubi = requestUser.getUser().isAzubi();

        // Some attributes may never be changed
        boolean constantsUnchanged = (oldNw.getAusbildungsjahr() == newNw.getAusbildungsjahr())
                && (oldNw.getAbteilung().equals(newNw.getAbteilung())
                && (oldNw.getStartDate().equals(newNw.getStartDate()))
                && (oldNw.getEndDate().equals(newNw.getEndDate()))
                && (oldNw.getAzubiId().equals(newNw.getAzubiId()))
                && (oldNw.getAusbilderId().equals(newNw.getAusbilderId()))
        );

        boolean activitiesChanged = (oldNw.getActivities().size() != newNw.getActivities().size());

        // May only modify Activities while the Nachweis is not signed in any way
        boolean mayChangeActivities = isAzubi
                && !(oldNw.isSignedByAzubi() || oldNw.isSignedByAusbilder());

        boolean signChangesOkay = checkSignChanges(oldNw, newNw, mayChangeActivities);

        return constantsUnchanged && ((!activitiesChanged || mayChangeActivities) && signChangesOkay);
    }

    private boolean checkSignChanges(AusbildungsNachweis oldNw, AusbildungsNachweis newNw, boolean smthElseChanged) {
        boolean isAzubi = requestUser.getUser().isAzubi();
        boolean isAusbilder = requestUser.getUser().isAusbilder();
        boolean oldAnySigned = (oldNw.isSignedByAzubi() || oldNw.isSignedByAusbilder());

        // Azubi may change other stuff if not signed before and sign instantly
        boolean mayAzubiChangeSign = (isAzubi && !oldNw.isSignedByAusbilder()
                && (!oldAnySigned || !smthElseChanged));

        boolean didAzubiChange = (oldNw.isSignedByAzubi() != newNw.isSignedByAzubi());

        if (didAzubiChange) {
            newNw.setAzubiSignDate(LocalDateTime.now());
            log.info("{} {}signed Nachweis {} ({} - {}) as Azubi",
                    requestUser.getUser().getEmail(),
                    newNw.isSignedByAzubi() ? "" : "un",
                    newNw.getId(),
                    newNw.getStartDate(),
                    newNw.getEndDate());
        }

        boolean mayAusbilderChangeSign = (isAusbilder
                && oldNw.isSignedByAzubi()
                && newNw.isSignedByAzubi());

        boolean didAusbilderChange = (oldNw.isSignedByAusbilder() != newNw.isSignedByAusbilder());

        if (didAusbilderChange) {
            newNw.setAusbilderSignDate(LocalDateTime.now());
            log.info("{} {}signed Nachweis {} ({} - {}) as Ausbilder",
                    requestUser.getUser().getEmail(),
                    newNw.isSignedByAusbilder() ? "" : "un",
                    newNw.getId(),
                    newNw.getStartDate(),
                    newNw.getEndDate());
        }

        return (mayAzubiChangeSign && didAzubiChange) || (mayAusbilderChangeSign && didAusbilderChange);
    }
}
