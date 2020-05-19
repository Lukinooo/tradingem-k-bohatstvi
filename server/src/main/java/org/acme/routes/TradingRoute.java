package org.acme.routes;

import io.vertx.core.http.HttpServerRequest;
import org.acme.models.Shop;
import org.acme.services.ShopManager;
import org.codehaus.jackson.map.ObjectMapper;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

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

    // TODO buy product by game product shop player (check account) (implement Lukas)
    // TODO sell product by game product shop player (check account) (implement Lukas)
}
