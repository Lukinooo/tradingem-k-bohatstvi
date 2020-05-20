package org.acme.services;

import org.acme.models.Game;
import org.acme.models.Player;
import org.acme.models.Shop;
import org.acme.persistence.GamePersist;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;


public class GameManager {
    private EntityManager em;

    public GameManager(EntityManager em) {
        this.em = em;
    }

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

        shopManager.initializeShops(game);
        shopManager.initializeProducts(game, game.getMax_products());
        return game;
    }

    public String addPlayer(String gameId, String playerName) {
        GamePersist gamePersist = new GamePersist(this.em);
        Game game = (Game) gamePersist.get(Long.parseLong(gameId));

        PlayerManager playerManager = new PlayerManager(em);
        Player player = playerManager.initializePlayer(game.getId().toString(), playerName, game.getPlayer_money());

        StringBuilder result = new StringBuilder();
        result.append("{\n\"playerId\": \"").append(player.getId()).append("\", ")
                .append("\"gameId\": \"").append(game.getId()).append("\", ")
                .append("\"status\": \"").append("ok\"")
//                .append(player.getName()).append(" joined into game \"")
//                .append("\" joined into game ")
//                .append(game.getName())
                .append("\n}");
        return result.toString();
    }

    public List listGame() {
        GamePersist gamePersist = new GamePersist(this.em);
        return gamePersist.getAll();
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
