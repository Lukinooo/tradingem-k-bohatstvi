package org.acme.services;

import org.acme.configs.GameConfig;
import org.acme.models.Game;
import org.acme.models.Player;
import org.acme.persistence.GamePersist;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;


public class GameManager {
    @Inject
    private GameConfig gameConfig;
    private EntityManager em;

    public GameManager(EntityManager em) {
        this.em = em;
    }

    public Game initializeGame(int maxPlayers, int maxShops, int maxProducts, String color, Float playerMoney,
                               Float longCenter, Float latCenter, Float radius, String gameName) {
        GamePersist gamePersist = new GamePersist(this.em);
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

        gameConfig.setPlayer_money(playerMoney);

        ShopManager shopManager = new ShopManager(em);
        shopManager.initializeShops(game);
        shopManager.initializeProducts(game);
        return game;
    }

    public String addPlayer(String gameName, String playerName) {
        GamePersist gamePersist = new GamePersist(this.em);
        Game game = (Game) gamePersist.getByName(gameName);

        PlayerManager playerManager = new PlayerManager(em);
        Player player = playerManager.initializePlayer(playerName, gameConfig.player_money);
        playerManager.addPlayerToGame(game, player);

        StringBuilder result = new StringBuilder();
        result.append("{\n\"playerId\": \"").append(player.getId()).append("\", ")
                .append("\"gameId\": \"").append(game.getId()).append("\", ")
                .append("\"status\": \"").append(player.getName())
                .append("\" joined into game ").append(game.getName()).append("\n}");
        return result.toString();
    }

    public List listGame() {
        GamePersist gamePersist = new GamePersist(this.em);
        return gamePersist.getAll();
    }

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
