package org.acme.services;

import org.acme.models.Game;
import org.acme.models.Player;
import org.acme.models.Shop;
import org.acme.persistence.PlayerPersist;
import redis.clients.jedis.Jedis;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlayerManager {
    private EntityManager em;

    public PlayerManager(EntityManager em) {
        this.em = em;
    }

    public Player initializePlayer(String gameId, String name, float money) {
        Player player = new Player();
        player.setName(name);

        PlayerPersist playerPersist = new PlayerPersist(em);
        playerPersist.create(player);

//        jedis.set("hrac:" + player.getId(), player.getName() + ":" + money);
//        System.out.println("hrac:" + player.getId());
//        System.out.println(jedis.get("hrac:" + player.getId()));
        this.setPlayerMoney(gameId, player, money);

        return player;
    }

    private void setPlayerMoney(String gameId, Player player, Float money) {
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.zadd("hra:" + gameId + ":hraci", money, player.getId() +  ":" + player.getName());
    }

    public List getAllPlayers(String gameId) {
        Jedis jedis = new Jedis("localhost", 6379);
        Set<String> result =  jedis.zrevrangeByScore("hra:" + gameId + ":hraci", 0, -1);
        return new ArrayList<>(result);
    }

    public String getPlayerScore(String gameId, String playerId) {
        PlayerPersist playerPersist = new PlayerPersist(em);
        Player player = (Player) playerPersist.get(Long.parseLong(playerId));

        Jedis jedis = new Jedis("localhost", 6379);
        Long position = jedis.zrank("hra:" + gameId + ":hraci", player.getId() +  ":" + player.getName());
        Double money = jedis.zscore("hra:" + gameId + ":hraci", player.getId() +  ":" + player.getName());

        return position.toString() + ":" + player.getName() + ":" + money.toString();
    }

    public Player updatePlayerScore(String gameId, String playerId, Float money) {
        PlayerPersist playerPersist = new PlayerPersist(em);
        Player player = (Player) playerPersist.get(Long.parseLong(playerId));

        this.setPlayerMoney(gameId, player, money);
        return player;
    }
}
