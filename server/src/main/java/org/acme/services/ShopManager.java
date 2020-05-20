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
import java.util.logging.Logger;

public class ShopManager {
    private EntityManager em;
    public Logger logger = Logger.getLogger("/logs/logger.log");

    public ShopManager(EntityManager em) {
        this.em = em;
    }

    /**
     * Function which initialize shops for game
     * the quantity of shops is based on given value in game object
     * it randomly generates latitude and longitude for each shop,
     * generates name for the shop and adds reference to game
     * and add these shops to database
     * the latitude and longitude is generated in given range (value radius in shop)
     *
     * @param game object for which the shops are generated
     */
    public void initializeShops(Game game) {
        logger.info("initializeShops: Create " + game.getMax_shops() + " new Shops in radius " + game.getRadius() + " into Game with " + game.getId());
        ShopPersist sp = new ShopPersist(em);
        ProductPersistence pp = new ProductPersistence(em);

        Long gameId = game.getId();
        Float latitudeCenter = game.getLatitude_center();
        Float longitudeCenter = game.getLongitude_center();
        int numShop = game.getMax_shops();
        Float radius = game.getRadius();
        System.out.println(radius);

        Float minLat = latitudeCenter - radius / 120000;
        Float maxLat = latitudeCenter + radius / 120000;
        Float minLon = longitudeCenter - radius / 150000;
        Float maxLon = longitudeCenter + radius / 150000;

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
            logger.info("initializeShops: Shop created");
        }
        game.setShops(shops);

        logger.info("initializeShops: Shops for Game with id " + game.getId() + " were created");
    }

    /**

     * Function which gets all products stored in the database
     *
     * @param em entity manager for hibernate
     * @return List of all products from database
     */
    public List getProducts(EntityManager em) {
        logger.info("getProducts: Get all products stored in the database");
        ProductPersistence pp = new ProductPersistence(em);
        return pp.getAll();
    }

    /**
     * Function which gets all products from given shop
     * these products are stored in redis
     * it parses our redis representations to json
     *
     * @param shopId id of shop, for which we want to get products
     * @return json string with all products for the shop
     */
    public String getShopProducts(String shopId) {
        logger.info("getShopProducts: Get all shop Products for Shop with shop id " + shopId);
        Jedis jedis = new Jedis("localhost", 6379);
        Map<String, String> products = jedis.hgetAll("obchod:" + shopId + ":produkty");

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

        logger.info("getShopProducts: All shop Products from Shop with id " + shopId + " are returned");

        return formated;
    }

    /**
     * Function which returns list off all shops which refer to given game id
     *
     * @param gameId id of game for which we want to get shops
     * @return list of all shops with equal game id as given
     */
    public List getAllShop(String gameId) {
        logger.info("getAllShop: Get all shop for game with game id " + gameId);
        ShopPersist shopPersist = new ShopPersist(em);
        List<Shop> shops = shopPersist.getAllById(Long.parseLong(gameId));
        return shops;
    }

    /**
     * Function which initialize products for all shops in given game
     * these products are initialized to redis
     * for that we use hash set
     * the key is product id and product name
     * the value is product category, product price and product count
     * the quantity of products is based on given value
     *
     * @param game         object for which shops we generate products
     * @param initialCount quantity of products in the shops
     */
    public void initializeProducts(Game game, int initialCount) {
        logger.info("initializeProducts: Initialize Products in Shops for Game with id " + game.getId() + " in count " + initialCount);
        ShopPersist shopPersist = new ShopPersist(em);
        List<Shop> shops = shopPersist.getAllById(game);
        List<Object> products = getProducts(em);
        PriceCategoryPersist priceCategoryPersist = new PriceCategoryPersist(em);

        Jedis jedis = new Jedis("localhost", 6379);
        Random rand = new Random();

        for (Shop shop : shops) {
            Set<Integer> indexes = randomGenerated(initialCount, products.size());

            logger.info("initializeProducts: Initialize Products in Shop with id " + shop.getId());

            for (Integer index : indexes) {
                Product prod = (Product) products.get(index);

                int price = (int) (rand.nextInt((int) ((prod.getPriceCategory().getMax_price() - prod.getPriceCategory().getMin_price()) + 1)) + prod.getPriceCategory().getMin_price());
                int prodCount = rand.nextInt((30 - 10) + 1) + 10;

                jedis.hset("obchod:" + shop.getId() + ":produkty", String.valueOf(prod.getId()) + ":" + String.valueOf(prod.getName()), prod.getPriceCategory().getId() + ":" + price + ":" + prodCount);
            }
        }

        logger.info("initializeProducts: Products initializes");
    }

    /**
     * Function which generates unique indexes in given range
     * this function is used for random selection of products to the shops
     *
     * @param count number which indicates how much numbers do we want
     * @param range number which indicates range in which thw indexes should be generated
     * @return set of integer indexes
     */
    public Set randomGenerated(int count, int range) {
        Random rand = new Random();
        Set<Integer> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.size();

        while (linkedHashSet.size() != count) {
            int n = rand.nextInt(range);
            linkedHashSet.add(n);
        }
        return linkedHashSet;
    }

    /**
     * Function which performs buy product mechanism
     * it get price of the product
     * calls function from GameMechanics
     * checks player account
     * updates player money
     *
     * @param gameId    game which is played
     * @param playerId  player which is buying stuff
     * @param shopId    shop from which player wants to buy something
     * @param productId product which playeer wants to buy
     * @return price of the product, which was bought or 0 if player dont have money
     */
    public String buyProduct(String gameId, String playerId, String shopId, String productId) {
        logger.info("buyProduct: Player with id " + playerId + " wants to buy product with id " + productId + " in shop with id " + shopId + " in game with id " + gameId);
        GameMechanic gameMechanic = new GameMechanic();

        PlayerPersist playerPersist = new PlayerPersist(em);
        Player player = (Player) playerPersist.get(Long.parseLong(playerId));

        ProductPersistence productPersistence = new ProductPersistence(em);
        Product product = (Product) productPersistence.get(Long.parseLong(productId));
        Float price = gameMechanic.checkPrice(shopId, productId, product.getName());

        logger.info("buyProduct: Product " + product.getName() + " price is" + price);

        if (price == (float) 0.0) {
            return "0";
        }

        if (gameMechanic.checkFinancial(gameId, player, price)) {
            price = gameMechanic.buyProduct(gameId, playerId, shopId, productId, product.getName());

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

            logger.info("buyProduct: Player is buying");

            return String.valueOf(money);
        } else {
            return "0";
        }
    }

    /**
     * Function which performs sell product mechanism
     * it get price of the product
     * calls function from GameMechanics
     * checks player account
     * updates player money
     *
     * @param gameId    game which is played
     * @param playerId  player which is buying stuff
     * @param shopId    shop from which player wants to buy something
     * @param productId product which playeer wants to buy
     * @return price of the product, which was bought or 0 if player dont have money
     */
    public String sellProduct(String gameId, String playerId, String shopId, String productId) {
        logger.info("sellProduct: Player with id " + playerId + " wants to sell product with id " + productId + " in shop with id " + shopId + " in game with id " + gameId);
        GameMechanic gameMechanic = new GameMechanic();

        PlayerPersist playerPersist = new PlayerPersist(em);
        Player player = (Player) playerPersist.get(Long.parseLong(playerId));

        ProductPersistence productPersistence = new ProductPersistence(em);
        Product product = (Product) productPersistence.get(Long.parseLong(productId));
        Float price = gameMechanic.checkPrice(shopId, productId, product.getName());

        logger.info("sellProduct: Product " + product.getName() + " price is" + price);

        if (gameMechanic.checkFinancial(gameId, player, price)) {
            price = gameMechanic.sellProduct(gameId, playerId, shopId, productId, product.getName());
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

            logger.info("sellProduct: Player is selling");

            return String.valueOf(money);
        } else {
            return "0";
        }
    }
}
