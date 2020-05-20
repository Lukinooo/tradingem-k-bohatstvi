package org.acme.mechanics;

import org.acme.models.Player;
import org.acme.persistence.ShopPersist;
import org.acme.services.ShopManager;
import redis.clients.jedis.Jedis;

import javax.persistence.EntityManager;
import java.text.DecimalFormat;

public class GameMechanic implements MechanicsLayer {
    public Boolean checkFinancial(String gameId, Player player, Float moneyToCheck) {
        Jedis jedis = new Jedis("localhost", 6379);
        Double money = jedis.zscore("hra:" + gameId + ":hraci", player.getId() +  ":" + player.getName());
        return money >= moneyToCheck;
    }

    /**
     * This function simulates buying process of products in the shop
     * it gets and splits element from redis to get values
     * it decrement count of product by 1 and increase the price
     * it also checks, if count is greater than 0
     * @param gameId id of the game which is played
     * @param playerId id of player who wants to buy product
     * @param shopId id of shop where player wants to buy product
     * @param productId id of product which he wants to buy
     * @param productName name od the product
     * @return float price which need to be subtract from players money or 0 if there is no product left, means the count
     * is 0
     */
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

    /**
     * This function simulates selling process of products in the shop
     * it gets and splits element from redis to get values
     * it increment count of product by 1 and decrease the price
     * @param gameId id of the game which is played
     * @param playerId id of player who wants to buy product
     * @param shopId id of shop where player wants to buy product
     * @param productId id of product which he wants to buy
     * @param productName name od the product
     * @return float price which need to be subtract from players money
     */
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

    /**
     * Function which returns product price in the shop
     * @param shopId shop in which is the product
     * @param productId the checked product
     * @param productName name of the checked product
     * @return price of the product
     */
    public Float checkPrice(String shopId, String productId, String productName) {
        Jedis jedis = new Jedis("localhost", 6379);
        String priceCount = jedis.hget("obchod:" + shopId + ":produkty", String.valueOf(productId) + ":" + productName);

        String[] splitPriceCount = priceCount.split(":");
        Float price = Float.parseFloat(splitPriceCount[1]);

        return price;
    }
}
