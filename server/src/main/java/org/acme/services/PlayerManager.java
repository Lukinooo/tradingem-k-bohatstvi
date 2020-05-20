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
import java.util.logging.Logger;

import static redis.clients.jedis.Protocol.Keyword.WITHSCORES;

public class PlayerManager {
    private EntityManager em;
    public Logger logger = Logger.getLogger("/logs/logger.log");

    public PlayerManager(EntityManager em) {
        this.em = em;
    }

    /**
     * Function which initialize player, add new into db, and add new player into redis
     *
     * @param gameId id of game
     * @param name   player name
     * @param money  player money
     * @return new Player
     */
    public Player initializePlayer(String gameId, String name, float money) {
        Player player = new Player();
        player.setName(name);

        PlayerPersist playerPersist = new PlayerPersist(em);
        playerPersist.create(player);
        logger.info("initializePlayer: Create new Player with name " + player.getName() + " and id "
                + player.getId() + " into game with " + gameId);

        this.setPlayerMoney(gameId, player, money);

        return player;
    }

    /**
     * Function which set player money to specific value (@param money)
     *
     * @param gameId id of game
     * @param player Player to update money
     * @param money  new value of money to update
     */
    private void setPlayerMoney(String gameId, Player player, Float money) {
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.zadd("hra:" + gameId + ":hraci", money, player.getId() + ":" + player.getName());
        logger.info("setPlayerMoney: add player (" + player.getId() + ") into redis");
    }

    /**
     * Function which gets all player in specific game
     *
     * @param gameId id of game
     * @return array of players score in json with playerId, playerName, playerMoney
     */
    public String getAllPlayers(String gameId) {
        Jedis jedis = new Jedis("localhost", 6379);
        Set<Tuple> redisResult = jedis.zrevrangeWithScores("hra:" + gameId + ":hraci", 0, -1);
        logger.info("getAllPlayers: redis return all player " + redisResult.size() + " in one specific game id " + gameId);

        List result = new ArrayList();

        String formated = null;
        ObjectMapper mapper = new ObjectMapper();

        ArrayNode arrayNode = mapper.createArrayNode();

        for (Tuple tuple : redisResult) {
            String[] element = tuple.getElement().split(":");

            ObjectNode objectNode1 = mapper.createObjectNode();
            objectNode1.put("playerId", element[0]);
            objectNode1.put("playerName", element[1]);
            objectNode1.put("playerMoney", tuple.getScore());

            arrayNode.add(objectNode1);
        }
        logger.info("getAllPlayers: successfully parsed set into array of JSON");
        formated = arrayNode.toString();
        return formated;
    }

    /**
     * Function which gets specific player score in specific game
     *
     * @param gameId   id of game
     * @param playerId id of player
     * @return score of player in game
     */
    public String getPlayerScore(String gameId, String playerId) {
        PlayerPersist playerPersist = new PlayerPersist(em);
        Player player = (Player) playerPersist.get(Long.parseLong(playerId));
        logger.info("getPlayerScore: get player with id " + player.getId() + " and name " + player.getName());

        Jedis jedis = new Jedis("localhost", 6379);
        Long position = jedis.zrank("hra:" + gameId + ":hraci", player.getId() + ":" + player.getName());
        Double money = jedis.zscore("hra:" + gameId + ":hraci", player.getId() + ":" + player.getName());
        logger.info("getPlayerScore: get position(" + position + "), money(" + money + ") of player " + player.getName());

        return position.toString() + ":" + player.getName() + ":" + money.toString();
    }


    /**
     * Function which update player score
     *
     * @param gameId   id of game
     * @param playerId id of player
     * @param money    money to update
     * @return Player which was updated
     */
    public Player updatePlayerScore(String gameId, String playerId, Float money) {
        PlayerPersist playerPersist = new PlayerPersist(em);
        Player player = (Player) playerPersist.get(Long.parseLong(playerId));
        logger.info("updatePlayerScore: get player with id " + player.getId() + " and name " + player.getName());

        this.setPlayerMoney(gameId, player, money);
        return player;
    }
}
