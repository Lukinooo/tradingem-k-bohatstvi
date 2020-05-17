package org.acme.services;

import org.acme.models.Player;
import org.acme.persistence.PlayerPersist;
import redis.clients.jedis.Jedis;

import javax.persistence.EntityManager;

public class PlayerManager {

    // TODO inicializacia playera Lukas - redis: peniaze, inicializacia pola na produkty, pripojenie na hru
    // TODO vytvorenie playera do db Lukas

    public void initializePlayer(EntityManager em, String name, int money) {
        Player player = new Player();
        player.setName(name);

        PlayerPersist pp = new PlayerPersist(em);
        pp.create(player);

        Jedis jedis = new Jedis("localhost", 6379);
        jedis.set("hrac:" + player.getId(), player.getName() + ":" + money);
        System.out.println("hrac:" + player.getId());
        System.out.println(jedis.get("hrac:" + player.getId()));
    }
}
