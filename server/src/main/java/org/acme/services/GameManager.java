package org.acme.services;

import org.acme.configs.GameConfig;
import org.acme.models.Game;
import org.acme.models.Shop;
import org.acme.persistence.GamePersist;
import org.acme.persistence.ShopPersist;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Random;

@Path("/")
public class GameManager {
    @Inject
    EntityManager em;
    @Inject
    GameConfig gameConfig;
    // DONE inicializacia obchodov Lukas - redis: priradenie produktov, ceny produktov - initializeShops ShopManager

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/initialize-game")
    @Transactional
    public String initializeGame() {

        return null;
    }


}
