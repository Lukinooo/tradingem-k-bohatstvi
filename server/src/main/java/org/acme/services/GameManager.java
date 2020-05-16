package org.acme.services;

import org.acme.configs.GameConfig;
import org.acme.models.Game;
import org.acme.persistence.GamePersist;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/create-game")
public class GameManager {
    @Inject
    EntityManager em;
    @Inject
    GameConfig gameConfig;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String getConfig() {
        GamePersist gamePersist = new GamePersist(em);
        Game game = new Game();
        game.setColor(gameConfig.color);
        game.setMax_player(gameConfig.players);
        game.setMax_products(gameConfig.products);
        game.setMax_shops(gameConfig.shops);
        game.setRadius(gameConfig.radius);
        Long id = gamePersist.create(game);
        System.out.println("Color id " + id);
        game.setColor("Blue");
        gamePersist.update(game);

        Game getGame = (Game) gamePersist.get(game.getId());
        System.out.println("Color by get " + getGame.getColor());
        gamePersist.delete(getGame);
//        em.persist(game);
        return game.getColor() + game.getMax_player();
    }
}
