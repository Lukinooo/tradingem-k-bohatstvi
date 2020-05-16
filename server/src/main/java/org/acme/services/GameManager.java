package org.acme.services;

import org.acme.model.Game;
import redis.clients.jedis.Jedis;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.nio.file.Paths;

@Path("/create-game")
public class GameManager {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getConfig() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Path currentPath = (Path) Paths.get(System.getProperty("user.dir"));
//        Path filePath = (Path) Paths.get(currentPath.toString(), "data", "GameConfig.json");
        return currentPath.toString();

//        JSONParser parser = new JSONParser();
//        try {
//            Object obj = parser.parse(new FileReader("..//GameConfig.json"));
//
//            // A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
//            JSONObject jsonObject = (JSONObject) obj;
//
//            // A JSON array. JSONObject supports java.util.List interface.
//            JSONArray companyList = (JSONArray) jsonObject.get("Company List");
//
//            // An iterator over a collection. Iterator takes the place of Enumeration in the Java Collections Framework.
//            // Iterators differ from enumerations in two ways:
//            // 1. Iterators allow the caller to remove elements from the underlying collection during the iteration with well-defined semantics.
//            // 2. Method names have been improved.
//            Iterator<JSONObject> iterator = companyList.iterator();
//            while (iterator.hasNext()) {
//                System.out.println(iterator.next());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
