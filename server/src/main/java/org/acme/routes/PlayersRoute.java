package org.acme.routes;

import io.vertx.core.http.HttpServerRequest;
import org.acme.configs.GameConfig;
import org.acme.models.Player;
import org.acme.services.PlayerManager;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.specimpl.MultivaluedTreeMap;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/")
public class PlayersRoute {
    @Inject
    EntityManager em;
    public static ObjectMapper mapper = new ObjectMapper();
    @Context
    HttpServerRequest request;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("update-player-money")
    @Transactional
    public String updatePlayerMoney() {
        String gameId = request.getParam("gameId");
        String playerId = request.getParam("playerId");
        Float money = Float.parseFloat(request.getParam("money"));

        PlayerManager playerManager = new PlayerManager(em);
        Player player = playerManager.updatePlayerScore(gameId, playerId, money);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\n\t\"status\": successfully,\n")
                .append("\t\"playerNamev\": ").append(player.getName()).append("\n}");
        return stringBuilder.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get-players-score")
    @Transactional
    public String getPlayersScore() {
        String gameId = request.getParam("gameId");

        PlayerManager playerManager = new PlayerManager(em);
        List sortedPlayers = playerManager.getAllPlayers(gameId);

        String result = null;
        try {
            result = mapper.writeValueAsString(sortedPlayers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get-player-score")
    @Transactional
    public String getPlayerScore() {
        String gameId = request.getParam("gameId");
        String playerId = request.getParam("playerId");

        PlayerManager playerManager = new PlayerManager(em);

        return playerManager.getPlayerScore(gameId, playerId);
    }
}
