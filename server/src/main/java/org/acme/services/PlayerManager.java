package org.acme.services;

import org.acme.models.Game;
import org.acme.models.Player;
import org.acme.models.Shop;
import org.acme.persistence.PlayerPersist;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import javax.persistence.EntityManager;
import java.util.*;

import static redis.clients.jedis.Protocol.Keyword.WITHSCORES;

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
        jedis.zadd("hra:" + gameId + ":hraci", money, player.getId() + ":" + player.getName());

    }

    public String getAllPlayers(String gameId) {
        Jedis jedis = new Jedis("localhost", 6379);
        Set<Tuple> redisResult = jedis.zrevrangeWithScores("hra:" + gameId + ":hraci", 0, -1);

        List result = new ArrayList();

        String formated = null;
        ObjectMapper mapper = new ObjectMapper();

        ArrayNode arrayNode = mapper.createArrayNode();

        for (Tuple tuple : redisResult) {
            String[] element = tuple.getElement().split(":");
            String score = new StringBuilder()
                    .append("{ \"playerId\": ").append(element[0]).append(", ")
                    .append("\"playerName\": ").append(element[1]).append(", ")
                    .append("\"playerMoney\": ").append(tuple.getScore()).append(" }")
                    .toString();
            result.add(score);

            ObjectNode objectNode1 = mapper.createObjectNode();
            objectNode1.put("playerId", element[0]);
            objectNode1.put("playerName", element[1]);
            objectNode1.put("playerMoney", tuple.getScore());

            arrayNode.add(objectNode1);
        }

        formated = arrayNode.toString();
        return formated;
    }

    public String getPlayerScore(String gameId, String playerId) {
        PlayerPersist playerPersist = new PlayerPersist(em);
        Player player = (Player) playerPersist.get(Long.parseLong(playerId));

        Jedis jedis = new Jedis("localhost", 6379);
        Long position = jedis.zrank("hra:" + gameId + ":hraci", player.getId() + ":" + player.getName());
        Double money = jedis.zscore("hra:" + gameId + ":hraci", player.getId() + ":" + player.getName());

        return position.toString() + ":" + player.getName() + ":" + money.toString();
    }

    public Player updatePlayerScore(String gameId, String playerId, Float money) {
        PlayerPersist playerPersist = new PlayerPersist(em);
        Player player = (Player) playerPersist.get(Long.parseLong(playerId));

        this.setPlayerMoney(gameId, player, money);
        return player;
    }
}
