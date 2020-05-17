package org.acme;

import redis.clients.jedis.Jedis;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/hello")
public class ExampleResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        Jedis jedis = new Jedis("localhost", 6379);
//        jedis.set("foo", "barbie");
//        String value = jedis.get("foo");

        jedis.rpush("my game", "Obchodik 1");
        jedis.rpush("my game", "Obchodik 2");
        jedis.rpush("my game", "Obchodik 3");
        List values = jedis.lrange("my game", 0, -1);

        for (Object value : values) {
            jedis = new Jedis("localhost", 6379);

            System.out.println(value);

            jedis.rpush((String) value, "Sosovicka");
            jedis.rpush((String) value, "Hrasok");
            jedis.rpush((String) value, "Fazula");

            System.out.println(jedis.lrange((String) value, 0, -1));
        }
        jedis.flushAll();
        jedis.close();

        return "hello" + " " + values;
    }

    // TODO add application logging
    // TODO add multiple languages
}