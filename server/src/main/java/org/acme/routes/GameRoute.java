package org.acme.routes;

import io.vertx.core.http.HttpServerRequest;
import org.acme.models.Game;
import org.acme.services.GameManager;
import org.codehaus.jackson.map.ObjectMapper;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/")
public class GameRoute {
    @Inject
    EntityManager em;
    public static ObjectMapper mapper = new ObjectMapper();
    @Context
    HttpServerRequest request;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get-start-time")
    @Transactional
    public String getStartTime(){
        String gameId = request.getParam("gameId");

        GameManager gameManager = new GameManager(em);
        return gameManager.getDateTime(gameId, "START").toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get-end-time")
    @Transactional
    public String getEndTime(){
        String gameId = request.getParam("gameId");

        GameManager gameManager = new GameManager(em);
        return gameManager.getDateTime(gameId, "END").toString();
    }
}
