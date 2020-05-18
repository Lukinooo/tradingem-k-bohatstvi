package org.acme.mechanics;

import org.acme.models.Player;
import redis.clients.jedis.Jedis;

public class GameMechanic implements MechanicsLayer {
    // TODO buy product by PlayerId, ShopId, ProductId (implement Lukas)

    // TODO sell product by PlayerId, ShopId, ProductId (implement Lukas)

    public Boolean checkFinancial(String gameId, Player player, Double moneyToCheck) {
        Jedis jedis = new Jedis("localhost", 6379);
        Double money = jedis.zscore("hra:" + gameId + ":hraci", player.getId() +  ":" + player.getName());
        return money >= moneyToCheck;
    }

    public void buyProduct(Long playerId, Long shopId, Long productId) {
        Jedis jedis = new Jedis("localhost", 6379);
        String priceCount = jedis.hget("obchod:" + shopId + ":produkty", String.valueOf(productId));

        String[] splitPriceCount = priceCount.split(":");
        Double price = Double.valueOf(splitPriceCount[0]);
        int count = Integer.parseInt(splitPriceCount[1]);

        price -= 0.1;
        count--;

        jedis.hset("obchod:" + shopId + ":produkty", String.valueOf(productId), price + ":" + count);

        System.out.println(jedis.hgetAll("obchod:" + shopId + ":produkty"));
    }

    public void sellProduct(Long playerId, Long shopId, Long productId) {
        Jedis jedis = new Jedis("localhost", 6379);
        String priceCount = jedis.hget("obchod:" + shopId + ":produkty", String.valueOf(productId));

        String[] splitPriceCount = priceCount.split(":");
        Double price = Double.valueOf(splitPriceCount[0]);
        int count = Integer.parseInt(splitPriceCount[1]);

        price += 0.1;
        count++;

        jedis.hset("obchod:" + shopId + ":produkty", String.valueOf(productId), price + ":" + count);
        System.out.println(jedis.hgetAll("obchod:" + shopId + ":produkty"));
    }

}
