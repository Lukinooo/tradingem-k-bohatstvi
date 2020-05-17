package org.acme.routes;

import io.vertx.core.http.HttpServerRequest;
import org.acme.configs.GameConfig;
import org.acme.services.GameManager;
import org.codehaus.jackson.map.ObjectMapper;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.acme.models.Game;
import java.io.IOException;
import java.util.List;

@Path("/")
public class StartRoute {
    @Inject
    GameConfig gameConfig;
    @Inject
    EntityManager em;
    public static ObjectMapper mapper = new ObjectMapper();
    @Context
    HttpServerRequest request;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("create-game")
    @Transactional
    public String createNewGame() {
        GameManager gameManager = new GameManager(em);

        int maxPlayer = Integer.parseInt(request.getParam("max_players"));
        int maxShops = Integer.parseInt(request.getParam("max_shops"));
        int maxProducts = Integer.parseInt(request.getParam("max_products"));

        String color = request.getParam("color");
        String gameName = request.getParam("game_name");

        float radius = Float.parseFloat(request.getParam("radius"));
        float playerMoney = Float.parseFloat(request.getParam("player_money"));
        float longitudeCenter = Float.parseFloat(request.getParam("longitude_center"));
        float latitudeCenter = Float.parseFloat(request.getParam("latitude_center"));

        Game game = gameManager.initializeGame(maxPlayer, maxShops, maxProducts,
                color, playerMoney, longitudeCenter, latitudeCenter, radius, gameName);

        String result = null;
        try {
            result = mapper.writeValueAsString(game);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("join-game")
    @Transactional
    public String joinPlayerIntoGame() {
        String playerName = request.getParam("player_name");
        String gameName = request.getParam("game_name");
        System.out.println(playerName + gameName);

        GameManager gameManager = new GameManager(em);
        return gameManager.addPlayer(gameName, playerName);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list-games")
    @Transactional
    public String getAllGames() {
        GameManager gameManager = new GameManager(em);
        List games = gameManager.listGame();

        String result = null;
        try {
            result = mapper.writeValueAsString(games);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("game-setting")
    @Transactional
    public String getGameSettings() {
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


//        String result = null;
//        try {
//            result = mapper.writeValueAsString(game);
//        } catch (JsonGenerationException | JsonMappingException e) {
//            e.printStackTrace();
//        }
//        System.out.println(request.getParam("gameName"));
//        System.out.println(request.getFormAttribute("playerName"));
//        System.out.println(request.getParam("playerName"));
//        Long id = gamePersist.create(game);
//        System.out.println("Color id " + id);
//        game.setColor("Blue");
//        gamePersist.update(game);
//
//        Game getGame = (Game) gamePersist.get(game.getId());
//        System.out.println("Color by get " + getGame.getColor());
//        gamePersist.delete(getGame);
//
//        return game.getColor() + game.getMax_player();
