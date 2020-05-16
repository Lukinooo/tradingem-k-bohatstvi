package org.acme;

import redis.clients.jedis.Jedis;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class ExampleResource {

//    @GET
//    @Produces(MediaType.TEXT_PLAIN)
//    public String hello() {
//        Jedis jedis = new Jedis("localhost", 6379);
//        jedis.set("foo", "barbie");
//        String value = jedis.get("foo");
//
//        return "hello" + " " + value;
//    }
}