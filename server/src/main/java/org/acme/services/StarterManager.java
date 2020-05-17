package org.acme.services;

import org.acme.configs.GameConfig;
import org.acme.models.Game;
import org.acme.persistence.GamePersist;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Path("/")
public class StarterManager {
    @Inject
    EntityManager em;
    @Inject
    GameConfig gameConfig;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/create-game")
    @Transactional
    public String createGame() {
        // TODO implement Matej
        GamePersist gamePersist = new GamePersist(em);
        Game game = new Game();
        game.setColor(gameConfig.color);
        game.setMax_player(gameConfig.players);
        game.setMax_products(gameConfig.products);
        game.setMax_shops(gameConfig.shops);
        game.setRadius(gameConfig.radius);
        game.setLatitude_center((float) 48.1344335);
        game.setLongitude_center((float) 17.2184426);
//        Long id = gamePersist.create(game);
//        System.out.println("Color id " + id);
//        game.setColor("Blue");
//        gamePersist.update(game);
//
//        Game getGame = (Game) gamePersist.get(game.getId());
//        System.out.println("Color by get " + getGame.getColor());
////        gamePersist.delete(getGame);
//
//        ShopManager gm = new ShopManager();
//        gm.initializeShops(getGame, em);

//        ShopManager gm = new ShopManager();
//
//        gm.getProducts(em);

        PlayerManager pm = new PlayerManager();
        pm.initializePlayer(em, "Vladko", 300);

        return game.getColor() + game.getMax_player();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/join-game")
    @Transactional
    public void joinGame() {
        // TODO implement Matej

    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/list-game")
    @Transactional
    public void listGame() {
        // TODO implement Matej

    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/game-setting")
    @Transactional
    public void gameSetting() {
        // TODO implement Matej

    }
}
