package org.acme.services;

import groovy.json.JsonBuilder;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.acme.configs.GameConfig;
import org.acme.models.Game;
import org.acme.persistence.GamePersist;

import javax.inject.Inject;
import javax.json.JsonBuilderFactory;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class StarterManager {
    @Inject
    private GameConfig gameConfig;
    private final GamePersist gamePersist;

    public StarterManager(EntityManager en) {
        this.gamePersist = new GamePersist(en);
    }

//@Path("/")
//    @Inject
//    EntityManager em;
//    @Inject
//    GameConfig gameConfig;
//    public static ObjectMapper mapper = new ObjectMapper();
//    private GamePersist gamePersist;
//    @Context
//    HttpServerRequest request;
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("create-game/{maxPlayers}/{color}/{maxShops}/{maxProducts}/{radius}/{gameName}/{latCenter}/{longCenter}")
//    @Transactional
//        String result = null;
//        try {
//            result = mapper.writeValueAsString(game);
//        } catch (JsonGenerationException | JsonMappingException e) {
//            e.printStackTrace();
//        }
//        System.out.println(request.getParam("gameName"));
//        System.out.println(request.getFormAttribute("playerName"));
//        System.out.println(request.getParam("playerName"));
//    Long id = gamePersist.create(game);
//        System.out.println("Color id " + id);
//        game.setColor("Blue");
//        gamePersist.update(game);
//
//    Game getGame = (Game) gamePersist.get(game.getId());
//        System.out.println("Color by get " + getGame.getColor());
//        gamePersist.delete(getGame);
//
//        return game.getColor() + game.getMax_player();

    public Game createGame(int maxPlayers, int maxShops, int maxProducts, String color,
                           Float longCenter, Float latCenter, Float radius, String gameName) {
        Game game = new Game();
        game.setColor(color);
        game.setMax_player(maxPlayers);
        game.setMax_products(maxProducts);
        game.setMax_shops(maxShops);
        game.setRadius(radius);
        game.setName(gameName);
        game.setLatitude_center(latCenter);
        game.setLongitude_center(longCenter);
        gamePersist.create(game);

        ShopManager shopManager = new ShopManager();
//        shopManager.initializeShops();
//        shopManager.initializeProducts();
        return game;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("join-game")
    @Transactional
    public String joinGame(String gameName, String playerName) {
        Game actualGame = (Game) gamePersist.getByName(gameName);

        PlayerManager playerManager = new PlayerManager();
//        int playerId = playerManager.joinPlayer(actualGame, playerName);

        return "{\n" +
                "\t'status': 'successfully'" + ",\n" +
                "\t'playerName': '" + playerName + "',\n" +
                "\t'playerId': '" + playerId + "',\n" +
                "\t'actualGame.ID': '" + actualGame.getId() + "'\n}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list-game")
    @Transactional
    public List listGame() throws IOException {
        return gamePersist.getAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("game-setting")
    @Transactional
    public String gameSetting() {
        StringBuilder config_value = new StringBuilder();
        config_value.append("{\n")
                    .append("color: ").append(gameConfig.color).append(",\n")
                    .append("max_players: ").append(gameConfig.players).append(",\n")
                    .append("max_shops: ").append(gameConfig.shops).append(",\n")
                    .append("max_products: ").append(gameConfig.products).append(",\n")
                    .append("radius: ").append(gameConfig.radius).append("\n}");

        return config_value.toString();
    }

}
