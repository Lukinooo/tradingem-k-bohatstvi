package org.acme.services;

import org.acme.models.Game;
import org.acme.models.Player;
import org.acme.models.Shop;
import org.acme.persistence.GamePersist;
import org.codehaus.jackson.node.ObjectNode;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;


public class GameManager {
    private EntityManager em;
    public Logger logger = Logger.getLogger("/logs/logger.log");

    public GameManager(EntityManager em) {
        this.em = em;
    }

    /**
     * Initialize game, create object Game, initialize Shop and initialize Products
     * @param maxPlayers maximum player, which can join into game
     * @param maxShops maximum shops in game
     * @param maxProducts maximum product in shops
     * @param color color of background in game view
     * @param playerMoney starting money for every player
     * @param longCenter longitude of playground
     * @param latCenter latitude of playground
     * @param radius radius of playground (rectangle)
     * @param gameName name of game
     * @param gameTime time when game will end
     * @return new Game object with Shops
     */
    public Game initializeGame(int maxPlayers, int maxShops, int maxProducts, String color, Float playerMoney,
                               Float longCenter, Float latCenter, Float radius, String gameName, int gameTime) {
        GamePersist gamePersist = new GamePersist(this.em);
        ShopManager shopManager = new ShopManager(em);
        Game game = new Game();

        game.setColor(color);
        game.setMax_player(maxPlayers);
        game.setMax_products(maxProducts);
        game.setMax_shops(maxShops);
        game.setRadius(radius);
        game.setName(gameName);
        game.setLatitude_center(latCenter);
        game.setLongitude_center(longCenter);
        game.setPlayerMoney(playerMoney);
        game.setCreated_at();
        game.setFinished_at(gameTime);
        gamePersist.create(game);
        logger.info("initializeGame: Create new Game with id " + game.getId());

        shopManager.initializeShops(game);
        logger.info("initializeGame: initialize Shops finished");
        shopManager.initializeProducts(game, game.getMax_products());
        logger.info("initializeGame: initialize Products finished");
        return game;
    }

    /**
     * Create new player and add into game by gameId
     * @param gameId id of game to join player
     * @param playerName player name
     * @return result about progress ( status, gameId, playerId )
     */
    public String addPlayer(String gameId, String playerName) {
        GamePersist gamePersist = new GamePersist(this.em);
        Game game = (Game) gamePersist.get(Long.parseLong(gameId));
        logger.info("addPlayer: get Game with id " + game.getId() + " and name " + game.getName());

        PlayerManager playerManager = new PlayerManager(em);
        Player player = playerManager.initializePlayer(game.getId().toString(), playerName, game.getPlayer_money());
        logger.info("addPlayer: initialize Player with id " + player.getId() + " and name " + player.getName());

        StringBuilder result = new StringBuilder();
//        ObjectNode objectNode = mapper.createObjectNode();
//        objectNode1.put("playerId", player.getId());
//        objectNode1.put("gameId", game.getId());
//        objectNode1.put("status", player.getName() + " joined into game " + game.getName())
//        return objectNode.toString();

        result.append("{\n\"playerId\": \"").append(player.getId()).append("\", ")
                .append("\"gameId\": \"").append(game.getId()).append("\", ")
                .append("\"status\": \"").append("ok\"")
//                .append(player.getName()).append(" joined into game \"")
//                .append("\" joined into game ")
//                .append(game.getName())
                .append("\n}");
        return result.toString();
    }

    /**
     * Get all games in db
     * @return list of Games with Shops
     */
    public List listGame() {
        GamePersist gamePersist = new GamePersist(this.em);
        List games = gamePersist.getAll();
        logger.info("listGame: get all Game results(" + games.size()+ ")" );
        return games;
    }

    public LocalDateTime getDateTime(String gameId, String type) {
        GamePersist gamePersist = new GamePersist(this.em);
        Game game = (Game) gamePersist.get(Long.parseLong(gameId));

        if (type.equals("END")) {
            return game.getFinished_at();
        }
        return game.getCreated_at();
    }
}
