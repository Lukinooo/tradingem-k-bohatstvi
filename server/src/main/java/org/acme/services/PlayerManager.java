package org.acme.services;

import org.acme.models.Game;
import org.acme.models.Player;
import org.acme.persistence.PlayerPersist;
import redis.clients.jedis.Jedis;

import javax.persistence.EntityManager;

public class PlayerManager {
    private EntityManager em;

    public PlayerManager(EntityManager em) {
        this.em = em;
    }

    public Player initializePlayer(String name, float money) {
        Player player = new Player();
        player.setName(name);

        PlayerPersist pp = new PlayerPersist(em);
        pp.create(player);

        Jedis jedis = new Jedis("localhost", 6379);
        jedis.set("hrac:" + player.getId(), player.getName() + ":" + money);
//        System.out.println("hrac:" + player.getId());
//        System.out.println(jedis.get("hrac:" + player.getId()));

        return player;
    }

    // TODO
    public void addPlayerToGame(Game game, Player player) {
    }
}
