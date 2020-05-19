package org.acme.services;

import org.acme.mechanics.GameMechanic;
import org.acme.models.Game;
import org.acme.models.Product;
import org.acme.models.Shop;
import org.acme.persistence.PriceCategoryPersist;
import org.acme.persistence.ProductPersistence;
import org.acme.persistence.ShopPersist;
import redis.clients.jedis.Jedis;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShopManager {
    private EntityManager em;

    public ShopManager(EntityManager em) {
        this.em = em;
    }

    public void initializeShops(Game game) {
        ShopPersist sp = new ShopPersist(em);
        ProductPersistence pp = new ProductPersistence(em);

        Long gameId = game.getId();
        Float latitudeCenter = game.getLatitude_center();
        Float longitudeCenter = game.getLongitude_center();
        int numShop = game.getMax_shops();
        Float radius = game.getRadius();

        Float minLat = latitudeCenter - radius / 10000;
        Float maxLat = latitudeCenter + radius / 10000;
        Float minLon = longitudeCenter - radius / 10000;
        Float maxLon = longitudeCenter + radius / 10000;

        Random r = new Random();
        List<Shop> shops = new ArrayList<>();

        for (int i = 0; i < numShop; i++) {
            Shop shop = new Shop();
            Float latitude = minLat + r.nextFloat() * (maxLat - minLat);
            Float longitude = minLon + r.nextFloat() * (maxLon - minLon);
            String name = "Obchodik " + i;

            shop.setGame(game);
            shop.setName(name);
            shop.setLatitude(latitude);
            shop.setLongitude(longitude);

            sp.create(shop);

            shops.add(shop);
        }
        game.setShops(shops);
    }

    public List getProducts(EntityManager em) {
        ProductPersistence pp = new ProductPersistence(em);
        return pp.getAll();
    }

    public List getShopProducts(Long shopId) {
        Jedis jedis = new Jedis("localhost", 6379);
        return jedis.lrange("obchod:" + shopId + ":produkty", 0, -1);
    }

    // TODO (implement Lukas or Matej)
    public Shop getShop(String gameId, String shopId) {
        Jedis jedis = new Jedis("localhost", 6379);
        return null;
    }

    // TODO (implement Lukas or Matej)
    public List getAllShop(String gameId) {
        Jedis jedis = new Jedis("localhost", 6379);
        List shops = jedis.lrange("hra:" + gameId + ":obchody", 0, -1);
        return shops;
    }

    // TODO implement Set product price in shop by ProductId, ShopId, Price (implement Lukas)

    public void initializeProducts(Game game) {
        ShopPersist shopPersist = new ShopPersist(em);
        List<Shop> shops = shopPersist.getAllById(game);
        List<Object> products = getProducts(em);
        PriceCategoryPersist priceCategoryPersist = new PriceCategoryPersist(em);
        List<Object> priceCategories = priceCategoryPersist.getAll();

        Jedis jedis = new Jedis("localhost", 6379);
        Random rand = new Random();
        GameMechanic gm = new GameMechanic();

        for (Shop shop : shops) {
            jedis.rpush("hra:" + game.getId() + ":obchody", String.valueOf(shop.getId()));

            for (Object product : products) {
                if ((rand.nextInt(100) & 1) == 1) {
                    Product prod = (Product) product;

                    int price = (int) (rand.nextInt((int) ((prod.getPriceCategory().getMax_price() - prod.getPriceCategory().getMin_price()) + 1)) + prod.getPriceCategory().getMin_price());

//                    jedis.rpush("obchod:" + shop.getId() + ":produkty", prod.getName() + ":" + prod.getPrice() + ":" + "10");
                    jedis.hset("obchod:" + shop.getId() + ":produkty", String.valueOf(prod.getId()), prod.getPriceCategory().getId() + ":" + price + ":" + "10");
                    gm.buyProduct((long) 1, shop.getId(), prod.getId());
                    gm.sellProduct((long) 1, shop.getId(), prod.getId());
                }
            }

//            System.out.println(jedis.hgetAll("obchod:" + shop.getId() + ":produkty"));
//            jedis.flushAll();




//            System.out.println(shopProducts(shop));
        }

//        System.out.println(jedis.lrange("hra:" + game.getId() + ":obchody", 0, -1));
//
//        System.out.println(shops);
    }
}
