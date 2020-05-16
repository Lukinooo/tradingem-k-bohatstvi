package org.acme.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "GAMES")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gameSeq")
    private Long id;
    private String name;
    private Float longitude_center;
    private Float latitude_center;
    private Float radius;
    private Float max_player;

    @OneToMany(mappedBy = "game")
    private List<Shop> shops;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
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

    public Float getMax_player() {
        return max_player;
    }

    public void setMax_player(Float max_player) {
        this.max_player = max_player;
    }
}
