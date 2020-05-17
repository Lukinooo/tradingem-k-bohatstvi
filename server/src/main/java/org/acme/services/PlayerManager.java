package org.acme.services;

import org.acme.models.Game;
import org.acme.models.Player;
import org.acme.models.Shop;
import org.acme.persistence.PlayerPersist;
import redis.clients.jedis.Jedis;

import javax.persistence.EntityManager;
import java.util.List;

public class PlayerManager {
    private EntityManager em;

    public PlayerManager(EntityManager em) {
        this.em = em;
    }

    public Player initializePlayer(String name, float money) {
        Player player = new Player();
        player.setName(name);

        PlayerPersist playerPersist = new PlayerPersist(em);
        playerPersist.create(player);

        Jedis jedis = new Jedis("localhost", 6379);
        jedis.set("hrac:" + player.getId(), player.getName() + ":" + money);
//        System.out.println("hrac:" + player.getId());
//        System.out.println(jedis.get("hrac:" + player.getId()));

        return player;
    }

    public void addPlayerToGame(Game game, Player player) {
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.rpush("hra:" + game.getId() + ":hraci",
                player.getId() + ":" + player.getName());
    }

    public List getPlayer(Game game) {
        Jedis jedis = new Jedis("localhost", 6379);
        return jedis.lrange("hra:" + game.getId() + ":hraci", 0, -1);
    }

    //TODO implement getAllPlayers by GameName -> SortList(Position, Name, Money) (implement Matej)

    //TODO implement showMyScore by Name or ID -> Position, Name, Money (implement Matej)

}
