package org.acme.mechanics;

import org.acme.models.Player;
import org.acme.persistence.ShopPersist;
import org.acme.services.ShopManager;
import redis.clients.jedis.Jedis;

import javax.persistence.EntityManager;
import java.text.DecimalFormat;

public class GameMechanic implements MechanicsLayer {
    // TODO buy product by PlayerId, ShopId, ProductId (implement Lukas)

    // TODO sell product by PlayerId, ShopId, ProductId (implement Lukas)

    public Boolean checkFinancial(String gameId, Player player, Float moneyToCheck) {
        Jedis jedis = new Jedis("localhost", 6379);
        Double money = jedis.zscore("hra:" + gameId + ":hraci", player.getId() +  ":" + player.getName());
        return money >= moneyToCheck;
    }

    public Float buyProduct(String gameId, String playerId, String shopId, String productId, String productName) {
        Jedis jedis = new Jedis("localhost", 6379);
        String priceCount = jedis.hget("obchod:" + shopId + ":produkty", String.valueOf(productId) + ":" + productName);

        String[] splitPriceCount = priceCount.split(":");
        int categoryId = Integer.parseInt(splitPriceCount[0]);
        Float price = Float.parseFloat(splitPriceCount[1]);
        Float newPrice = Float.valueOf(splitPriceCount[1]);
        int count = Integer.parseInt(splitPriceCount[2]);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        if (count > 0) {
            newPrice += Float.parseFloat(df.format((float) categoryId / 10));
            newPrice = Float.parseFloat(df.format(newPrice));
            count--;
        }
        else {
            return (float) 0.0;
        }

        jedis.hset("obchod:" + shopId + ":produkty", String.valueOf(productId) + ":" + productName, categoryId + ":" + newPrice + ":" + count);
        return price;
    }

    public Float sellProduct(String gameId, String playerId, String shopId, String productId, String productName) {
        Jedis jedis = new Jedis("localhost", 6379);
        String priceCount = jedis.hget("obchod:" + shopId + ":produkty", String.valueOf(productId) + ":" + productName);

        String[] splitPriceCount = priceCount.split(":");
        int categoryId = Integer.parseInt(splitPriceCount[0]);
        Float price = Float.parseFloat(splitPriceCount[1]);
        Double newPrice = Double.valueOf(splitPriceCount[1]);
        int count = Integer.parseInt(splitPriceCount[2]);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        newPrice -= Double.parseDouble(df.format((float) categoryId / 10));
        newPrice = Double.parseDouble(df.format(newPrice));
        count++;

        jedis.hset("obchod:" + shopId + ":produkty", String.valueOf(productId) + ":" + productName, categoryId + ":" + newPrice + ":" + count);
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
