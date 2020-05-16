package org.acme.services;

import org.acme.configs.GameConfig;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class GameManager {
    @Inject
    EntityManager em;
    @Inject
    GameConfig gameConfig;
    // TODO vytvorit obchody ku hre Lukas
    // TODO rozmiestnenie obchodov podla radiusu Lukas
    // TODO inicializacia playera Lukas
    // TODO inicializacia obchov Lukas

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/initialize-game")
    @Transactional
    public String initialize_game() {

        return null;
    }
}
