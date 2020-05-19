package org.acme.mechanics;

import org.acme.models.Player;
import org.acme.persistence.ShopPersist;
import org.acme.services.ShopManager;
import redis.clients.jedis.Jedis;

import javax.persistence.EntityManager;

public class GameMechanic implements MechanicsLayer {
    // TODO buy product by PlayerId, ShopId, ProductId (implement Lukas)

    // TODO sell product by PlayerId, ShopId, ProductId (implement Lukas)

    public Boolean checkFinancial(String gameId, Player player, Float moneyToCheck) {
        Jedis jedis = new Jedis("localhost", 6379);
        Double money = jedis.zscore("hra:" + gameId + ":hraci", player.getId() +  ":" + player.getName());
        return money >= moneyToCheck;
    }

    public Float buyProduct(String gameId, String playerId, String shopId, String productId) {
        Jedis jedis = new Jedis("localhost", 6379);
        String priceCount = jedis.hget("obchod:" + shopId + ":produkty", String.valueOf(productId));

        String[] splitPriceCount = priceCount.split(":");
        int categoryId = Integer.parseInt(splitPriceCount[0]);
        Float price = Float.parseFloat(splitPriceCount[1]);
        Double newPrice = Double.valueOf(splitPriceCount[1]);
        int count = Integer.parseInt(splitPriceCount[2]);

        if (count > 0) {
            newPrice += 0.1 * categoryId;
            count--;
        }

        jedis.hset("obchod:" + shopId + ":produkty", String.valueOf(productId), categoryId + ":" + newPrice + ":" + count);
        return price;
    }

    public Float sellProduct(String gameId, String playerId, String shopId, String productId) {
        Jedis jedis = new Jedis("localhost", 6379);
        String priceCount = jedis.hget("obchod:" + shopId + ":produkty", String.valueOf(productId));

        String[] splitPriceCount = priceCount.split(":");
        int categoryId = Integer.parseInt(splitPriceCount[0]);
        Float price = Float.parseFloat(splitPriceCount[1]);
        Double newPrice = Double.valueOf(splitPriceCount[1]);
        int count = Integer.parseInt(splitPriceCount[2]);

        newPrice -= 0.1 * categoryId;
        count++;

        jedis.hset("obchod:" + shopId + ":produkty", String.valueOf(productId), categoryId + ":" + newPrice + ":" + count);
        return price;
    }



//    public void takeMoney(String gameId, String playerId, Float money) {
//        this.checkFinancial(gameId, )
//    }
//
//    public void giveMoney(String gameId, String playerId, Float money) {
//
//    }
}
