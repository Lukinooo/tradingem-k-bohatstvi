package org.acme.routes;

import io.vertx.core.http.HttpServerRequest;
import org.acme.configs.GameConfig;
import org.acme.models.Player;
import org.acme.services.PlayerManager;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.specimpl.MultivaluedTreeMap;
import redis.clients.jedis.Tuple;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.*;

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
        String gameId = request.getParam("game_id");
        String playerId = request.getParam("player_id");
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
        String gameId = request.getParam("game_id");

        PlayerManager playerManager = new PlayerManager(em);
        String result = playerManager.getAllPlayers(gameId);
        // TODO return as JSON - DONE snad, neviem uplne


        return result;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get-player-score")
    @Transactional
    public String getPlayerScore() {
        String gameId = request.getParam("game_id");
        String playerId = request.getParam("player_id");


        PlayerManager playerManager = new PlayerManager(em);
        String[] splitScore = playerManager.getPlayerScore(gameId, playerId).split(":");
        StringBuilder result = new StringBuilder();
        result.append("{\n").append("\t\"position\": ").append(splitScore[0]).append(" ,\n")
                .append("\t\"playerName\": ").append(splitScore[1]).append(" ,\n")
                .append("\t\"playerMoney\": ").append(splitScore[2]).append(" \n}");

        return result.toString();
    }
}
