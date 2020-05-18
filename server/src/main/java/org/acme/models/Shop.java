package org.acme.models;

import javax.persistence.*;

@Entity
@Table(name = "SHOPS")
public class Shop {
    @Id
    @GeneratedValue(generator = "sequence_shop_id")
    private Long id;
    private String name;
    private Integer num_products;
    private Float latitude;
    private Float longitude;

    @ManyToOne
    @JoinColumn(name="game_id", nullable=false)
    private Game games;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNum_products() {
        return num_products;
    }

    public void setNum_products(Integer num_products) {
        this.num_products = num_products;
    }

    public Game getGame() {
        return games;
    }

    public void setGame(Game game) {
        this.games = game;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Game getGames() {
        return games;
    }

    public void setGames(Game games) {
        this.games = games;
    }
}

