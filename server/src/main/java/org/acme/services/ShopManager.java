package org.acme.services;

import org.acme.mechanics.GameMechanic;
import org.acme.models.Game;
import org.acme.models.Player;
import org.acme.models.Product;
import org.acme.models.Shop;
import org.acme.persistence.PlayerPersist;
import org.acme.persistence.PriceCategoryPersist;
import org.acme.persistence.ProductPersistence;
import org.acme.persistence.ShopPersist;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import redis.clients.jedis.Jedis;

import javax.persistence.EntityManager;
import java.lang.reflect.Array;
import java.util.*;

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
        System.out.println(radius);

        Float minLat = latitudeCenter - radius / 100000;
        Float maxLat = latitudeCenter + radius / 100000;
        Float minLon = longitudeCenter - radius / 130000;
        Float maxLon = longitudeCenter + radius / 130000;

        Random r = new Random();
        List<Shop> shops = new ArrayList<>();

        for (int i = 0; i < numShop; i++) {
            Shop shop = new Shop();
            Float latitude = minLat + r.nextFloat() * (maxLat - minLat);
            Float longitude = minLon + r.nextFloat() * (maxLon - minLon);
            System.out.println(latitude);
            System.out.println(longitude);
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

    public String getShopProducts(String shopId) {
        Jedis jedis = new Jedis("localhost", 6379);
        Map<String, String> products =  jedis.hgetAll("obchod:" + shopId + ":produkty");

        String formated = null;
        ObjectMapper mapper = new ObjectMapper();

        ArrayNode arrayNode = mapper.createArrayNode();

        for (Map.Entry<String, String> product : products.entrySet()) {
            String productString = product.getKey() + ":" + product.getValue();
            String[] productElements = productString.split(":");

            ObjectNode objectNode1 = mapper.createObjectNode();
            objectNode1.put("id", productElements[0]);
            objectNode1.put("name", productElements[1]);
            objectNode1.put("category", productElements[2]);
            objectNode1.put("price", productElements[3]);
            objectNode1.put("count", productElements[4]);

            arrayNode.add(objectNode1);
        }

        formated = arrayNode.toString();
        return formated;
    }

    public List getAllShop(String gameId) {
        ShopPersist shopPersist = new ShopPersist(em);
        List<Shop> shops = shopPersist.getAllById(Long.parseLong(gameId));
        return shops;
    }

    public void initializeProducts(Game game, int initialCount) {
        ShopPersist shopPersist = new ShopPersist(em);
        List<Shop> shops = shopPersist.getAllById(game);
        List<Object> products = getProducts(em);
        PriceCategoryPersist priceCategoryPersist = new PriceCategoryPersist(em);

        Jedis jedis = new Jedis("localhost", 6379);
        Random rand = new Random();

        for (Shop shop : shops) {
            Set<Integer> indexes = randomGenerated(initialCount, products.size());

            for (Integer index : indexes) {
                Product prod = (Product) products.get(index);

                int price = (int) (rand.nextInt((int) ((prod.getPriceCategory().getMax_price() - prod.getPriceCategory().getMin_price()) + 1)) + prod.getPriceCategory().getMin_price());

                jedis.hset("obchod:" + shop.getId() + ":produkty", String.valueOf(prod.getId()) + ":" + String.valueOf(prod.getName()), prod.getPriceCategory().getId() + ":" + price + ":" + initialCount);
            }
        }
    }

    public Set randomGenerated(int count, int range) {
        Random rand = new Random();
        Set<Integer> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.size();

        while (linkedHashSet.size() != count){
            int n = rand.nextInt(range);
            linkedHashSet.add(n);
        }
        return linkedHashSet;
    }

    public String buyProduct(String gameId, String playerId, String shopId, String productId) {
        GameMechanic gameMechanic = new GameMechanic();

        PlayerPersist playerPersist = new PlayerPersist(em);
        Player player = (Player) playerPersist.get(Long.parseLong(playerId));

        ProductPersistence productPersistence = new ProductPersistence(em);
        Product product = (Product) productPersistence.get(Long.parseLong(productId));
        Float price = gameMechanic.buyProduct(gameId, playerId, shopId, productId, product.getName());
        if (price == (float) 0.0) {
            return "0";
        }

        if (gameMechanic.checkFinancial(gameId, player, price)) {

            PlayerManager playerManager = new PlayerManager(em);
            String playerMoney = playerManager.getPlayerScore(gameId, playerId);
            String[] playerInfo = playerMoney.split(":");
            int position = Integer.parseInt(playerInfo[0]);
            String name = playerInfo[1];
            Float money = Float.parseFloat(playerInfo[2]);

            System.out.println(position);
            System.out.println(name);
            System.out.println(money);

            money -= price;

            player = playerManager.updatePlayerScore(gameId, playerId, money);

            return String.valueOf(money);
        }
        else {
            return "0";
        }
    }

    public String sellProduct(String gameId, String playerId, String shopId, String productId) {
        GameMechanic gameMechanic = new GameMechanic();

        PlayerPersist playerPersist = new PlayerPersist(em);
        Player player = (Player) playerPersist.get(Long.parseLong(playerId));

        ProductPersistence productPersistence = new ProductPersistence(em);
        Product product = (Product) productPersistence.get(Long.parseLong(productId));
        Float price = gameMechanic.sellProduct(gameId, playerId, shopId, productId, product.getName());

        if (gameMechanic.checkFinancial(gameId, player, price)) {
            PlayerManager playerManager = new PlayerManager(em);
            String playerMoney = playerManager.getPlayerScore(gameId, playerId);

            String[] playerInfo = playerMoney.split(":");
            int position = Integer.parseInt(playerInfo[0]);
            String name = playerInfo[1];
            Float money = Float.parseFloat(playerInfo[2]);

            System.out.println(position);
            System.out.println(name);
            System.out.println(money);

            money += price;

            player = playerManager.updatePlayerScore(gameId, playerId, money);

            return String.valueOf(money);
        }
        else {
            return "0";
        }
    }
}
