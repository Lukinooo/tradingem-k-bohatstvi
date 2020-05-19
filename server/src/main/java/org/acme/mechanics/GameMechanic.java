package org.acme.mechanics;

import org.acme.models.Player;
import org.acme.persistence.ShopPersist;
import org.acme.services.ShopManager;
import redis.clients.jedis.Jedis;

public class GameMechanic implements MechanicsLayer {
    // TODO buy product by PlayerId, ShopId, ProductId (implement Lukas)

    // TODO sell product by PlayerId, ShopId, ProductId (implement Lukas)

    public Boolean checkFinancial(String gameId, Player player, Double moneyToCheck) {
        Jedis jedis = new Jedis("localhost", 6379);
        Double money = jedis.zscore("hra:" + gameId + ":hraci", player.getId() +  ":" + player.getName());
        return money >= moneyToCheck;
    }

    public String buyProduct(String gameId, String playerId, String shopId, String productId) {
        Jedis jedis = new Jedis("localhost", 6379);
        if (jedis.hexists("obchod:" + shopId + ":produkty", String.valueOf(productId))) {
            Float price = this.setProductPrice(Long.parseLong(playerId), Long.parseLong(shopId), Long.parseLong(productId), "buy");
//            this.takeMoney(gameId, playerId, price);
        }

        return jedis.hget("obchod:" + shopId + ":produkty", String.valueOf(productId));
    }

    public String sellProduct(String gameId, String playerId, String shopId, String productId) {
        Jedis jedis = new Jedis("localhost", 6379);
        if (jedis.hexists("obchod:" + shopId + ":produkty", String.valueOf(productId))) {
            Float price = this.setProductPrice(Long.parseLong(playerId), Long.parseLong(shopId), Long.parseLong(productId), "sell");
        }

        return jedis.hget("obchod:" + shopId + ":produkty", String.valueOf(productId));
    }

    public Float setProductPrice(Long playerId, Long shopId, Long productId, String method) {
        Jedis jedis = new Jedis("localhost", 6379);
        String priceCount = jedis.hget("obchod:" + shopId + ":produkty", String.valueOf(productId));

        String[] splitPriceCount = priceCount.split(":");
        int categoryId = Integer.parseInt(splitPriceCount[0]);
        Float price = Float.parseFloat(splitPriceCount[1]);
        Double newPrice = Double.valueOf(splitPriceCount[1]);
        int count = Integer.parseInt(splitPriceCount[2]);

        if ((method == "buy") && (count > 0)) {
            newPrice += 0.1 * categoryId;
            count--;
        }
        else if (method == "sell") {
            newPrice -= 0.1 * categoryId;
            count++;
        }

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
