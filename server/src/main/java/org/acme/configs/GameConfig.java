package org.acme.configs;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "game")
public class GameConfig {
    @ConfigProperty(name = "color")
    public  String color;

    @ConfigProperty(name = "players")
    public  int players;

    @ConfigProperty(name = "shops")
    public  int shops;

    @ConfigProperty(name = "products")
    public  int products;

    @ConfigProperty(name = "radius")
    public  Float radius;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public int getShops() {
        return shops;
    }

    public void setShops(int shops) {
        this.shops = shops;
    }

    public int getProducts() {
        return products;
    }

    public void setProducts(int products) {
        this.products = products;
    }

    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }
}
