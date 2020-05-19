package org.acme.routes;

import io.vertx.core.http.HttpServerRequest;
import org.acme.mechanics.GameMechanic;
import org.acme.models.Shop;
import org.acme.services.ShopManager;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/")
public class TradingRoute {

    @Inject
    EntityManager em;
    public static ObjectMapper mapper = new ObjectMapper();
    @Context
    HttpServerRequest request;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get-shops")
    @Transactional
    public String getShops() {
        String gameId = request.getParam("game_id");

        ShopManager shopManager = new ShopManager(em);
        List shops = shopManager.getAllShop(gameId);

        String result = null;
        try {
            result = mapper.writeValueAsString(shops);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get-shop")
    @Transactional
    public String getShop() {
        String gameId = request.getParam("game_id");
        String shopId = request.getParam("shop_id");


        ShopManager shopManager = new ShopManager(em);
        Shop shop = shopManager.getShop(gameId, shopId);

        String result = null;
        try {
            result = mapper.writeValueAsString(shop);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get-shop-products")
    @Transactional
    public String getShopProducts() {
        String shopId = request.getParam("shop_id");

        ShopManager shopManager = new ShopManager(em);
        String result = shopManager.getShopProducts(shopId);

        return result;
    }

    // TODO buy product by game product shop player (check account) (implement Lukas)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("buy-product")
    @Transactional
    public String buyProduct() {
        String gameId = request.getParam("game_id");
        String playerId = request.getParam("player_id");
        String shopId = request.getParam("shop_id");
        String productId = request.getParam("product_id");

        ShopManager shopManager = new ShopManager(em);
        String status = shopManager.buyProduct(gameId, playerId, shopId, productId);

        String result = null;
        try {
            result = mapper.writeValueAsString(status);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    // TODO sell product by game product shop player (check account) (implement Lukas)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("sell-product")
    @Transactional
    public String sellProduct() {
        String gameId = request.getParam("game_id");
        String playerId = request.getParam("player_id");
        String shopId = request.getParam("shop_id");
        String productId = request.getParam("product_id");

        ShopManager shopManager = new ShopManager(em);
        String status = shopManager.sellProduct(gameId, playerId, shopId, productId);

        String result = null;
        try {
            result = mapper.writeValueAsString(status);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
