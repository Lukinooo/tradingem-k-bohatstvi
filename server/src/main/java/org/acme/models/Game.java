package org.acme.models;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "GAMES")
public class Game implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Float longitude_center;
    private Float latitude_center;
    private Float radius;
    private int max_player;
    private int max_shops;
    private int max_products;
    private String color;
    private float player_money;
    private LocalDateTime created_at;
    private LocalDateTime finished_at;
    @OneToMany(targetEntity=Shop.class, mappedBy="games", fetch=FetchType.EAGER)
    private List<Shop> shops;

    public float getPlayer_money() {
        return player_money;
    }

    public void setPlayer_money(float player_money) {
        this.player_money = player_money;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at() {
        this.created_at = LocalDateTime.now();
    }

    public LocalDateTime getFinished_at() {
        return finished_at;
    }

    public void setFinished_at(int gameTime) {
        this.finished_at = LocalDateTime.now();
        this.finished_at = this.finished_at.plusHours(gameTime);
    }

    public float getPlayerMoney() {
        return player_money;
    }

    public void setPlayerMoney(float playerMoney) {
        this.player_money = playerMoney;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getLongitude_center() {
        return longitude_center;
    }

    public void setLongitude_center(Float longitude_center) {
        this.longitude_center = longitude_center;
    }

    public Float getLatitude_center() {
        return latitude_center;
    }

    public void setLatitude_center(Float latitude_center) {
        this.latitude_center = latitude_center;
    }

    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    public int getMax_player() {
        return max_player;
    }

    public void setMax_player(int max_player) {
        this.max_player = max_player;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMax_shops() {
        return max_shops;
    }

    public void setMax_shops(int max_shops) {
        this.max_shops = max_shops;
    }

    public int getMax_products() {
        return max_products;
    }

    public void setMax_products(int max_products) {
        this.max_products = max_products;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Shop> getShops() {
        return shops;
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", longitude_center=" + longitude_center +
                ", latitude_center=" + latitude_center +
                ", radius=" + radius +
                ", max_player=" + max_player +
                ", max_shops=" + max_shops +
                ", max_products=" + max_products +
                ", color='" + color + '\'' +
                ", shops=" + shops +
                '}';
    }
}
